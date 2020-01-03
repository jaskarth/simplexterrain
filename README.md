# Simplex Terrain
Makes new terrain with multiple octaves of simplex noise!

## What is Simplex Terrain Generation?
Simplex Terrain Generation is a world generation mod focused on giving users the ability to extensively customize the terrain generation and generating that terrain quickly. 
To use Simplex Terrain Generation, select the world type `Simplex Terrain` when making a new world. Server owners will want to put `simplex` as the level type in server.properties. Simplex Terrain Generation 0.4.0+ requires Fabric API.

## Configuration
When launching the game for the first time, you will get a new file in your config folder called simplexterrain.json. In that you will find a plethora of values to tweak your terrain generation. Play around with it and find what suits you best!  
The config file is documented [here!](https://github.com/SuperCoder7979/simplexterrain/wiki/Config-Documentation)  
Here are some really cool [config presets](https://github.com/SuperCoder7979/simplexterrain/wiki/Config-Presets) for you to use!  
Different noise algorithms that you can use are covered in detail [here!](https://github.com/SuperCoder7979/simplexterrain/wiki/Noise-Implementations)  

## Discord server
Join our **Discord server** https://discord.gg/BuBGds9 to get the latest updates on what we're doing, to get help, or to simply talk about the cool words that you've made! If you'd like to help contribute, please contact us here.

## Specific Implementation Details
Simplex Terrain Generation uses a heightmap made out of simplex noise, normalized to fit between 256 and 0. This heightmap is used to place the actual blocks and to place the biomes. The biomes are placed in 7 distinct "regions", to make the terrain more natural. Here's a list of them:

Y0 - Y29: All variants of deep ocean  
Y30 - Y60: All variants of ocean  
Y63 - Y66: Beach  
From Y67 onwards, biome generation is split into 4 different regions based on height (Y67 - Y90, Y91 - Y140, Y141 - Y190, Y191 - Y256). The biomes can vary, but generally become colder as the height increases. If you would like to increase the amount of cold/hot biomes, a config for that will be available in 0.5.0 (unreleased at time of writing).  If you want to change the heights that these regions start at, take a look at the config!

## Images  
Standard simplex noise:  
![](https://cdn.discordapp.com/attachments/608088354042544139/649758293291696139/unknown.png "")
Perlin noise:  
![](https://cdn.discordapp.com/attachments/546812532070023186/652293723962736650/unknown.png)
Cubic noise:  
![](https://cdn.discordapp.com/attachments/651607986661228599/652270019014819850/unknown.png)
Worley noise:  
![](https://cdn.discordapp.com/attachments/546812532070023186/652303110051594250/unknown.png)
Foothills noise preset: (Learn more about presets [on the GitHub wiki](https://github.com/SuperCoder7979/simplexterrain/wiki/Config-Presets/_edit))  
![](https://cdn.discordapp.com/attachments/651608793880461322/652333136335732756/unknown.png)

#Disclaimers
This project is heavily in development. This means things can break and there will be bugs. A forge port will not be made of this mod (by me at least). If you want to port the mod yourself, go ahead! 

##Credits

SuperCoder79 - Breaks everything  
Valoeghese - World Generation Magician  
