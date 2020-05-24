#include <stdio.h>
#include "view.h"
#include "minecraft.h"
#include "input.h"

#define WIDTH 1280
#define HEIGHT 720

void sendInput(GLFWwindow* window, int key, int scancode, int action, int mods) {
	input->keyPress(window, key, scancode, action, mods);
}

int main() {

	view->setResolution(WIDTH, HEIGHT);
	view->create();//should be last!!!!!

	game->create();

	while (!glfwWindowShouldClose(view->getWindow())) {
		view->tick();
		game->tick();
		glfwSetKeyCallback(view->getWindow(), sendInput);
	}
	
	game->destroy();
	view->destroy();

	return 0;
}