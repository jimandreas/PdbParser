PdbParser
==========

Experimental library for parsing PDB files.

<br><b>status:</b> in development.  Not yet posted for sharing

License
---------
Apache.  <i>See LICENSE.txt</i>

Credits
-------
The focus of the PDB work is on study of PDB files hosted by the
[RCSB][1].  Testing of the work has been carried out on a subset of
PDB files featured in the 20 years of the <b>PDB101</b> monthly 
online publication also hosted by the [RCSB][1].

  [1]: https://www.rcsb.org/

- Bond information

The bond table information derives from pdbe database information, 
for example from the query:

    https://www.ebi.ac.uk/pdbe/api/pdb/compound/bonds/dg
    
For more information, see:

https://www.ebi.ac.uk/about/terms-of-use/

The bond table has been debugged using the PDB101 sample subset to include 
bonds not present in the EBI dataset.
    
Build technology credits
------------------------

- jakewharton/Picnic - for nice maven central publication structure

- MarcinMoskala/KotlinDiscreteMathToolkit - for nice testing structure

Documentation technology credits
--------------------------------

The documentation uses `MkDocs` framework and the `material` theme provided 
kindly by Martin Donath (github/squidfunk).

https://squidfunk.github.io/mkdocs-material/
<br>https://www.mkdocs.org/

    
 
