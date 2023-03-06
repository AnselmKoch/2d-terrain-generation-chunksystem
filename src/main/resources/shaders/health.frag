#version 330 core

in vec2 oTex;
in vec3 oPos;
in float fTexID;
in vec4 oColor;

uniform sampler2D tex_sample[8];

out vec4 color;

void main(){
    int id = int(fTexID);
    color = oColor;
}