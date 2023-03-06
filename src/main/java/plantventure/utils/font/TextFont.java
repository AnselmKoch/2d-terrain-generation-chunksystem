package plantventure.utils.font;

import plantventure.graphics.game.font.FontRenderer;
import plantventure.graphics.texture.Texture;
import plantventure.utils.LoggerUtils;
import plantventure.utils.Position;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TextFont {
    private static final Logger logger = LoggerUtils.getLogger(TextFont.class);

    private File fontFile;
    private InputStream fontFileInput;
    private URL fontFileURL;
    private URL fontImageURL;
    private Texture fontTexture;
    private Map<String, RenderChar[]> cachedTexts;

    private float fontAspecRatio = 1.0f / 512f;

    private Map<Character, Glyph> glyphMap;

    public TextFont(String name) {
        String path = "/fonts/" + name;
        this.fontFileURL = this.getClass().getResource(path + ".fnt");
        this.fontImageURL = this.getClass().getResource(path + ".png");
        this.fontFileInput = this.getClass().getResourceAsStream(path + ".fnt");
        this.fontTexture = new Texture("fonts/" + name);
        this.glyphMap = new HashMap<>();
        this.cachedTexts = new HashMap<>();

        try {
            readFile(fontFileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawTextCentered(String text, Vector3f position, float width, float height, float size, Vector4f color) {
        RenderChar[] renderChars = new RenderChar[text.length()];

        float totalWidth = 0.0f;
        for(int i = 0; i < text.length(); i++) {

            if(text.charAt(i) == ' ') {
                totalWidth += 0.3 * size;
                continue;
            }

            Glyph glyph = glyphMap.get(text.charAt(i));
            float charWidth = glyph.width * size;
            totalWidth += charWidth;
        }

        totalWidth = totalWidth - totalWidth/ width * size;

        float currX = position.x - (totalWidth / 2f), currY = position.y + (height /2) * size ;
        for(int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                currX += 0.3 *size;
                continue;
            }


            Glyph glyph = glyphMap.get(c);

            float xTex = glyph.x * fontAspecRatio, yTex = glyph.y * fontAspecRatio;
            float yHeight = glyph.height * fontAspecRatio,xWidth = glyph.width * fontAspecRatio;
            Vector2f[] texCords = new Vector2f[]{
                    new Vector2f(xTex + xWidth, yTex),
                    new Vector2f(xTex + xWidth, yTex + yHeight),
                    new Vector2f(xTex, yTex + yHeight),
                    new Vector2f(xTex, yTex),

            };

            float charWidth = glyph.width  * size;
            float charHeight = glyph.height * size;
            float charXOffset = glyph.xOffset * size;
            float charYOffset = glyph.yOffset * size;

            RenderChar renderChar = new RenderChar(c,
                    new Vector3f(currX + charXOffset, currY- charYOffset - charHeight, 1.0f),
                    texCords, charWidth, charHeight, 1.0f, fontTexture, Position.BOTTOMLEFT, color);

            renderChars[i] = renderChar;

            currX += charWidth + charXOffset;
        }
        FontRenderer.addText(renderChars);
    }

    public void drawText(String name, String text, Vector3f position, float width, float height, Vector4f color, boolean override) {
        RenderChar[] renderChars = new RenderChar[text.length()];

        float currX = position.x, currY = position.y;
         for(int i = 0; i < text.length(); i++) {
           char c = text.charAt(i);
           if (c == ' ') {
               currX += 0.3f * width;
               continue;
           }


           Glyph glyph = glyphMap.get(c);

           float xTex = glyph.x * fontAspecRatio, yTex = glyph.y * fontAspecRatio;
           float yHeight = glyph.height * fontAspecRatio,xWidth = glyph.width * fontAspecRatio;
           Vector2f[] texCords = new Vector2f[]{
                   new Vector2f(xTex + xWidth, yTex),
                   new Vector2f(xTex + xWidth, yTex + yHeight),
                   new Vector2f(xTex, yTex + yHeight),
                   new Vector2f(xTex, yTex),

           };

           float charWidth = glyph.width / width;
           float charHeight = glyph.height / height;
           float charXOffset = glyph.xOffset / width;
           float charYOffset = glyph.yOffset / height;


           RenderChar renderChar = new RenderChar(c,
                   new Vector3f(currX + charXOffset, currY- charYOffset - charHeight, 1.0f),
                   texCords, charWidth, charHeight, 1.0f, fontTexture, Position.BOTTOMLEFT, color);

           renderChars[i] = renderChar;

           currX += charWidth + charXOffset;
       }
        FontRenderer.addText(renderChars);
    }

    /**
     * Reads every info for every char inside the font textfile and creates a Glyph for every char with its information
     * @throws IOException
     */
    private void readFile(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lastLine = "";
        while (lastLine != null) {
            if (lastLine.startsWith("char ")) {
                String[] output = lastLine.split(" ");
                ArrayList<String> strings = new ArrayList<>();

                for (String string : output) {
                    if (!string.trim().isEmpty() && string.contains("=")) {
                        strings.add(string);
                    }
                }
                float[] values = new float[8];
                for (int i = 0; i < strings.size(); i++) {
                    //char
                    if (i == 0) {
                        values[i] = Integer.valueOf(strings.get(0).replace("id=", ""));
                    }
                    //X
                    if (i == 1) {
                        values[i] = Integer.valueOf(strings.get(1).replace("x=", ""));
                    }
                    //Y
                    if (i == 2) {
                        values[i] = Integer.valueOf(strings.get(2).replace("y=", ""));
                    }
                    //width
                    if (i == 3) {
                        values[i] = Integer.valueOf(strings.get(3).replace("width=", ""));
                    }
                    //height
                    if (i == 4) {
                        values[i] = Integer.valueOf(strings.get(4).replace("height=", ""));
                    }
                    //xoffset
                    if (i == 5) {
                        values[i] = Integer.valueOf(strings.get(5).replace("xoffset=", ""));
                    }
                    //yoffset
                    if (i == 6) {
                        values[i] = Integer.valueOf(strings.get(6).replace("yoffset=", ""));
                    }
                    //xadvance
                    if (i == 7) {
                        values[i] = Integer.valueOf(strings.get(7).replace("xadvance=", ""));
                    }
                }


                Glyph glyph = new Glyph(values[1], values[2], values[3], values[4], values[5], values[6]);
                char ascii = (char) values[0];
                glyphMap.put(ascii, glyph);
            }
            lastLine = bufferedReader.readLine();
        }
    }

    public File getFontFile() {
        return fontFile;
    }

    public Texture getFontTexture() {
        return this.fontTexture;
    }
    public Map<Character, Glyph> getGlyphMap() {
        return glyphMap;
    }
    private boolean isTextCached(String name) {
        Object foundText = this.cachedTexts.get(name);
        return foundText != null;
    }
}