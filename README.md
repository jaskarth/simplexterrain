# Simplex Terrain
Makes new terrain with multiple octaves of simplex noise!

## What is Simplex Terrain Generation?
Simplex Terrain Generation is a fabric mod that totally changes how minecraft terrain is made! Simplex Terrain Generation uses multiple octaves of Simplex noise to create valleys and peaks unlike vanilla terrain gen. With this mod, biomes don't control the terrain-- the terrain controls the biomes.
To use Simplex Terrain Generation, select the world type `Simplex Terrain` when making a new world. Server owners will want to put `simplex` as the level type in server.properties.

## Specific Implementation Details
Simplex Terrain Generation uses a heightmap made out of 11 octaves of simplex noise, normalized to fit (usually) between 256 and 0. This heightmap is used to place the actual blocks and place the biomes. The biomes are placed in 7 distinct "regions", to make the terrain more natural. Here's a list of them, denoted by the height of the terrain that they spawn in:

Y0 - Y29: All variants of deep ocean  
Y30 - Y60: All variants of ocean  
Y61 - Y68: Beach  
Y69 - Y90: Plains, Desert, Swamp, Savannah, and Jungle  
Y91 - Y140: Forest, Birch forest, and Dark forest  
Y141 - Y200: Snowy Taiga  
Y200 - Y256: Mountains  

##Images
![](https://cdn.discordapp.com/attachments/608088354042544139/649758293291696139/unknown.png "")
![](https://cdn.discordapp.com/attachments/546812532070023186/649767151627927553/unknown.png "")
![](https://cdn.discordapp.com/attachments/546812532070023186/649767773597204505/unknown.png "")

## Disclaimers
This project is heavily in development. This means things can break and there will be bugs. A forge port will not be made of this mod (by me at least). If you want to port the mod yourself, go ahead!
Notable issues currently: 

* Only about 10 biomes in total spawn. This is intended and I'm looking into better ways to place biomes.  
* There's limited flat land. In the future the mountains will be a bit more spread apart than they are currently.  
* Oceans can sometimes go to bedrock and mountains can sometimes go up to the height limit. This will be fixed soon too.  
* Villages spawn in some... *interesting* locations. This is a vanilla issue and can't easily be solved without some major time and effort.  

## Credits
SuperCoder79 - Idea and code  
Valoeghese - Help with some code
