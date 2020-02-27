## SuperCoder's random reference for worldgen stuff


### Subchunk Position

`v` = x or z position  
Subchunk: `v >> 2`  
to blockpos: `v << 2`  
from blockpos to position in chunk: `v & 15`

### Caching
* Cache noise modifiers (called multiple times per blockpos)
* Do not cache post processors (called once per blockpos)