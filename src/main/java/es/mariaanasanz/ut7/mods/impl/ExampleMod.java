package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(DamMod.MOD_ID)
public class ExampleMod extends DamMod implements IBlockBreakEvent, IServerStartEvent,
        IItemPickupEvent, ILivingDamageEvent, IUseItemEvent, IFishedEvent,
        IInteractEvent, IMovementEvent {

    public ExampleMod(){
        super();
    }

    @Override
    public String autor() {
        return "Javier Jorge Soteras";
    }

    @Override
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        System.out.println("Bloque destruido en la posicion "+pos);
    }

    @Override
    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        LOGGER.info("Server starting");
    }

    @Override
    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        LOGGER.info("Item recogido");
        System.out.println("Item recogido");
    }

    @Override
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        System.out.println("evento LivingDamageEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());
    }

    @Override
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        System.out.println("evento LivingDeathEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());

    }

    @Override
    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LOGGER.info("evento LivingEntityUseItemEvent invocado "+event.getEntity().getClass());
    }


    @Override
    @SubscribeEvent
    public void onPlayerFish(ItemFishedEvent event) {
        System.out.println("¡Has pescado un pez!");
    }

    @Override
    @SubscribeEvent
    public void onPlayerWalk(MovementInputUpdateEvent event) {
        if(event.getEntity() instanceof Player){
            if(event.getInput().down){
                System.out.println("down"+event.getInput().down);
            }
            if(event.getInput().up){
                System.out.println("up"+event.getInput().up);
            }
            if(event.getInput().right){
                System.out.println("right"+event.getInput().right);
            }
            if(event.getInput().left){
                System.out.println("left"+event.getInput().left);
            }
        }
    }


    @Override
    @SubscribeEvent
    public void onPlayerTouch(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        Level world = event.getLevel();
        String nombreHerramienta = player.getMainHandItem().getDisplayName().getString().toLowerCase();
        if (nombreHerramienta.endsWith("pickaxe]")){
            if (state.toString().toLowerCase().contains("grass_block") || state.toString().toLowerCase().contains("dirt")) {
                if (nombreHerramienta.contains("wood")){
                    this.crearTunel(pos,55,world);
                } else if (nombreHerramienta.contains("stone")) {
                    this.crearTunel(pos,45,world);
                }else if (nombreHerramienta.contains("gold")) {
                    this.crearTunel(pos,35,world);
                }else if (nombreHerramienta.contains("iron")) {
                    this.crearTunel(pos,25,world);
                }else if (nombreHerramienta.contains("diamond")) {
                    this.crearTunel(pos,15,world);
                }else{
                    this.crearTunel(pos,5,world);
                }
            }
        }
    }
    private void crearTunel(BlockPos pos, int nivel, Level world) {
        Block aire = Blocks.AIR;
        Block escaleras = Blocks.STONE_STAIRS;
        Block cristal = Blocks.GLASS;
        Block antorcha = Blocks.WALL_TORCH;
        int izqDer = pos.getX();
        int delAtras = pos.getZ() + 1;
        int altura = pos.getY();
        if (altura > nivel) {
            for (int i = altura; i >= nivel; i--) {
                for (int j = izqDer - 1; j <= izqDer + 1; j++) {
                    for (int k = delAtras; k < delAtras + 4; k++) {
                        world.setBlock(new BlockPos(j, i, k), aire.defaultBlockState(), 3);
                    }
                    world.setBlock(new BlockPos(j, i, delAtras - 1), escaleras.defaultBlockState(), 3);
                }
                for (int j = delAtras - 1; j < delAtras + 4; j++) {
                    world.setBlock(new BlockPos(izqDer - 2, i, j), cristal.defaultBlockState(), 3);
                    world.setBlock(new BlockPos(izqDer + 2, i, j), cristal.defaultBlockState(), 3);
                }
                for (int j = izqDer - 2; j <= izqDer + 2; j++) {
                    if (i > nivel+3) {
                        world.setBlock(new BlockPos(j, i, delAtras + 4), cristal.defaultBlockState(), 3);
                        if (i % 5 == 0 && j == izqDer) {
                            world.setBlock(new BlockPos(j, i, delAtras + 3), antorcha.defaultBlockState(), 3);
                        }
                    } else if (i == nivel+3) {
                        world.setBlock(new BlockPos(j, i, delAtras + 4), cristal.defaultBlockState(), 3);
                        world.setBlock(new BlockPos(j, i, delAtras + 5), cristal.defaultBlockState(), 3);
                        world.setBlock(new BlockPos(j, i, delAtras + 6), cristal.defaultBlockState(), 3);
                        if (j == izqDer) {
                            world.setBlock(new BlockPos(j, i, delAtras + 3), antorcha.defaultBlockState(), 3);
                        }
                    } else {
                        if (i==nivel+2) {
                            world.setBlock(new BlockPos(j, i, delAtras + 4), aire.defaultBlockState(), 3);
                            world.setBlock(new BlockPos(j, i, delAtras + 5), aire.defaultBlockState(), 3);
                            if (j == izqDer - 2 || j == izqDer + 2) {
                                world.setBlock(new BlockPos(j, i, delAtras + 4), cristal.defaultBlockState(), 3);
                                world.setBlock(new BlockPos(j, i, delAtras + 5), cristal.defaultBlockState(), 3);
                            }
                        } else if (i == nivel+1) {
                            world.setBlock(new BlockPos(j, i, delAtras + 4), aire.defaultBlockState(), 3);
                            if (j == izqDer - 2 || j == izqDer + 2) {
                                world.setBlock(new BlockPos(j, i, delAtras + 4), cristal.defaultBlockState(), 3);
                            }
                        }
                    }
                }
                delAtras++;
            }
        }
    }
}
