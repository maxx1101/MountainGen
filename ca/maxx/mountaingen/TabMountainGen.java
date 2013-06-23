package ca.maxx.mountaingen;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMountainGen {

	//Create new Creative Tab for MountainGen Mod
	public static CreativeTabs tabMountainGen = new CreativeTabs("tabMountainGen") {
        public ItemStack getIconItemStack() {
                return new ItemStack(MountainGen.mountainBlock, 1, 0);
        }
	};
}
