#pragma once
#include "player.h"
#include "block.h"

class Minecraft {
public:

	Minecraft();

	void create();
	void tick();
	void destroy();

private:

	Player* player;
	Block* block;

};

extern Minecraft* game;