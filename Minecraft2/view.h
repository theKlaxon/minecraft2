#pragma once
#include <GLFW/glfw3.h>

class View {
public:

	View();

	void create();
	void destroy();
	void tick();
	void housekeeping();
	void setResolution(int w, int h);

	int getWidth();
	int getHeight();

	GLFWwindow* getWindow();

private:

	int width;
	int height;

	GLFWwindow* window;
	GLuint programID;
	GLuint LoadShaders(const char* vertex_file_path, const char* fragment_file_path);
};

extern View* view;