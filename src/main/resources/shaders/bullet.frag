#version 330 core

in vec2 oTex;
in vec3 oPos;
in float fTexID;
in vec4 oColor;

uniform sampler2D tex_sample[19];

out vec4 color;

void main(){
    int id = int(fTexID);
    color = oColor * texture(tex_sample[id], oTex);
}