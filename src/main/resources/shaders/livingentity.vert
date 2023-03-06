#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aTex;
layout (location=2) in float aTexId;
layout (location=3) in vec4 aColor;

uniform mat4 uModel;
uniform mat4 uPersp;

out vec2 oTex;
out vec3 oPos;
out float fTexID;
out vec4 oColor;

void main()
{
    fTexID = aTexId;
    oPos = aPos;
    oTex = aTex;
    oColor = aColor;

    gl_Position = uPersp * uModel * vec4(aPos, 1.0);
}
