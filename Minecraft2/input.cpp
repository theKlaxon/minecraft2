#include <stdio.h>
#include <GLFW/glfw3.h>
#include "input.h"

Input::Input() {

}

void Input::keyPress(GLFWwindow* window, int key, int scancode, int action, int mods) {
	/*if (key == GLFW_KEY_W && action == GLFW_PRESS)
		activate_airship();*/ //EXAMPLE

	if (key == GLFW_KEY_W && action == GLFW_PRESS)
		printf("W");
	if (key == GLFW_KEY_A && action == GLFW_PRESS)
		printf("A");
	if (key == GLFW_KEY_S && action == GLFW_PRESS)
		printf("S");
	if (key == GLFW_KEY_D && action == GLFW_PRESS)
		printf("D");
}

Input* input = new Input();