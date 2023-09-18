/**
 * StrHashTable class. 
 * 
 * StrHashTable is a hashtable data structure that stores a key value pair of strings in a hashtable.
 * StrHashTable uses a hash function to create an index from the key in order to store the value string.
 * @version 0.0.0.1
 */
public class StrHashTable {
    //load factor of the hashtable of 75%
    private double load = 0.75;

    //an array of nodes for the hashtbale
    private Node[] hashtable;
    //the size of the hashtable
    private int size;

    //The number of full entries to the hashtable
    int numFullEntries;
    //The number of collisions to the hashtable
    int numCollisions;
    //The number of rehashes of  the hashtable
    int numRehashes;


    /**
     * Hashtable Constructor
     * intialises a hashtable to the size passed in, and intialises numFullEntries, numRehashes, numCollisions to zero
     * @param s The size to set the hashtable to as an integer
     */
    public StrHashTable(int s){
        //Initialise local variables and create the hashtable
        size = s;
        hashtable = new Node[size];

        numFullEntries = 0;
        numCollisions = 0;
        numRehashes = 0;
    }


    /**
     * Node Class.
     * An inner class of StrHashTable that contains the key and value of the hash table elements.
     * Contains a constructor for a Node object.
     * Contains a getKey() method to return the key .
     * Contains a getValue() method to return the value
     */
    private class Node{
        //String to generate the index of the hashtable entry
        private String key;
        //String to generate the value of the hashtable entry
        private String value;


        /**
         * Node Constructor
         * Intialises a node object to the key and value strings passed in
         * @param k The key of the node, used to generate the index 
         * @param v The value of the node, the string to be stored in the table
         */
        public Node(String k, String v){
            this.key = k;
            this.value = v;
        }

        /**
         * getKey.
         * Returns the private member variable key
         * @return String the private member variable key
         */
        public String getKey(){
            return key;
        }

        /**
         * getValue.
         * Returns the private member variable value
         * @return String the private member variable value
         */
        public String getValue(){
            return value;
        }
    }


    /**
     * insert.
     * Inserts a Node, consisting of a key value pair of strings into the hashtable.
     * Rehashes the hashtable if the load factor is passed.
     * @param k The key of the node, used to generate the index
     * @param v The value of the node, the string to be stored in the table
     */
    public void insert(String k, String v){
        //Check if the load factor has been reached
        if((double)numFullEntries / size > load){
            //refactor 
            rehash();
            numRehashes++;
        }


        //get the index via the hash function
        int index = hashFunction(k);

        //check if the table is empty, insert if it is
        if(hashtable[index] == null){
            Node node = new Node(k, v);
            hashtable[index] = node;
            numFullEntries++;
        }
        else{
            Node node = new Node(k, v);
            hashtable[index] = node;
            //keeptrack of collisions;
            numCollisions++;
        }
    }

    /**
     * delete.
     * Deletes a Node from the hashtable that contains the key given as a parameter.
     * @param k The key of the node to be deleted
     */
    public void delete(String k){
        //get the index by calling the hash function
        int index = hashFunction(k);

        //if the index isnt empty and is what we want delete it;
        if(hashtable[index] != null && hashtable[index].getKey().equals(k)){
            //delete it from the table and decrement the number of full entries
            hashtable[index] = null;
            numFullEntries--;
        }
    }


    /**
     * contains.
     * Checks if the hashtable contains the item with the key given as a parameter
     * @param k The key to be checked if contianed in the hashtable
     * @return Boolean true if the Node with the given key is found, false otherwise
     */
    public boolean contains(String k){
        //Get the index from the hashFunction
        int index = hashFunction(k);

        //if the key appears return true
        if(hashtable[index] != null && hashtable[index].getKey().equals(k)){
            return true;
        }
        return false;

    }

    /**
     * isEmpty.
     * Checks if the hashtable is empty
     * 
     * @return Boolean true if the hashtable is empty, false otherwise
     */
    public boolean isEmpty(){
        if(numFullEntries == 0){
            return true;
        }
        return false;
    }

    /**
     * size.
     * Gives the number of non-null entries that exist in the table
     * @return  Int of the number of non-null entries to the table
     */
    public int size(){
        return numFullEntries;
    }

    /**
     * dump.
     * Prints each key value pair of nodes in the hashtable to the standard output
     */
    public void dump(){
        for(int i = 0; i < hashtable.length; i++){
            //if its empty print null
            if(hashtable[i] == null){
                System.out.println(i + ": null, null");
            }
            else{
                //print the key and value
                Node n = hashtable[i];
                System.out.println(i + ": " + n.getKey() + ", " + n.getValue());
            }
        }
    }


    /**
     * get.
     * Gets the value of the entry with the same key as the parameter given
     * @param k The key of the entry, from which its value is to be returned.
     * @return String of the value of the Node with the given key
     */
    public String get(String k){
        //get the index from the hashFunction
        int index = hashFunction(k);

        //if the item is in the hashtable return it otherwise return null
        if(hashtable[index] != null && hashtable[index].getKey().equals(k)){
            Node n = hashtable[index];
            return n.getValue();
        }
        return null;
    }


    /**
     * rehash.
     * Rehashes the hastable by doubling its size and reinserting the
     * key value pairs to the new table
     */
    public void rehash(){
        //make a copy of the hash table
        Node[] hashtableCopy = hashtable;

        //increase the original table size by 2
        size *= 2;
        hashtable = new Node[size];

        //reset number of full entries to zero
        numFullEntries = 0;

        //Copy the contents of the copied table to the new table
        for(int i = 0; i < hashtableCopy.length; i++){
            //if the table entry is not empty adding it to new table
            if(hashtableCopy[i] != null){
                Node node = hashtableCopy[i];
                insert(node.getKey(), node.getValue());
            }
        }
    }


    /**
     * hashFunction.
     * Turns the key passed in to the integer index used in the hashtable, by using the folding strings method
     * @param k The key to be converted to an integer 
     * @return  Int of the index to be used in the hashtable
     */
    public int hashFunction(String k){
        int sum  = 0;
        //grouping of all ascii values from k
        StringBuilder asciiGroup = new StringBuilder();
        //Array for ascii values of k split into groups of 3
        String[] sumGroup;

        //Loop through each character in the key
        for(int i = 0; i < k.length(); i++){
            //Convert the character to ascii
            int asciiValue = (int)k.charAt(i);
            //add the ascii value to our asciiGroup
            asciiGroup.append(asciiValue);

            //add a space for every third ascii entry
            if((i+1) % 3 == 0){
                asciiGroup.append(" ");
            }
        }

        //Split the ascii values by the space character into an array
        sumGroup = asciiGroup.toString().split(" ");
        
        //loop through the sum group array and add the values up
        for(String item : sumGroup){
            //add the value to the sum
            sum += Integer.parseInt(item);
        }

        //return the sum modulo the size to get the index
        return sum % size;
    }
    
}
