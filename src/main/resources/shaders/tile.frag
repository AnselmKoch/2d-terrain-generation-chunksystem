#version 330 core

in vec2 oTex;
in vec3 oPos;
in float fTexID;
in vec4 oColor;


uniform sampler2D tex_sample[19];
uniform bool drawText = true;

out vec4 color;

void main(){
      if(drawText) {
            int id = int(fTexID);
            color = oColor * texture(tex_sample[id], oTex);
      }else{
            int id = int(fTexID);
            color = oColor *  texture(tex_sample[id], oTex);
      }
}