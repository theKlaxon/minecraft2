#pragma once
extern class GLFWwindow;

class Input {
public:

	Input();

	void keyPress(GLFWwindow* window, int key, int scancode, int action, int mods);

};

extern Input* input;