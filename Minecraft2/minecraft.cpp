#include <stdlib.h>
#include "minecraft.h"

Minecraft::Minecraft() {
	player = new Player();
	block = new Block();

}

void Minecraft::create() {
	block->create();
}

void Minecraft::tick() {
	player->tick();
	block->render();
}

void Minecraft::destroy() {

}

Minecraft* game = new Minecraft();