import gr.ntua.ivml.mint.db.Meta;

// List Meta properties

list = DB.datasetDAO.findAll(); // put the objects you want to see here

for(Object o: list) {
    println(Meta.getAllProperties(o));
}
