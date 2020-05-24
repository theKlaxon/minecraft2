#pragma once

class Player {
public:

	Player();

	void create();
	void tick();
	void destroy();

	void move();

	int getWidth();
	int getHeight();

private:

	int width;
	int height;
};
