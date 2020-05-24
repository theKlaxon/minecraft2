#pragma once


class Block {
public:

	Block();

	void create();
	void housekeeping();
	void render();
	void destroy();

private:

	bool renderable;

	const char* defaultTexture = "game/terrain/terrain.png";

};