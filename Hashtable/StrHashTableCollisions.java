import java.util.ArrayList;
import java.util.List;
/**
 * StrHashTableCollisions class. 
 * 
 * StrHashTableCollisions  is a hashtable data structure that stores a key value pair of strings in a hashtable.
 * It uses a hash function to create an index from the key in order to store the value string.
 * Deals with the index collisions when inserting by adding the items to a list.
 * @version 0.0.0.1
 */
public class StrHashTableCollisions {
    //load factor of the hashtable of 75%
    private double load = 0.75;
    //a list of buckets for the hashtable
    private List<Bucket>[] hashtable;
    //the size of the hashtable
    private int size;
    //The number of full entries to the hashtable
    private int numFullEntries;
    //The number of collisions to the hashtable
    private int numCollisions;
    //The number of rehashes of  the hashtable
    private int numRehashes;


    /**
     * StrHashTableCollisions Constructor
     * intialises a hashtable capable of collision resolving, to the size passed in, and intialises numFullEntries, numRehashes, numCollisions to zero
     * @param s Int the size to set the hashtable to
     */
    public StrHashTableCollisions(int s) {
        //Initialise local variables and create the hashtable
        size = s;
        hashtable = new ArrayList[size];
        numFullEntries = 0;
        numCollisions = 0;
        numRehashes = 0;
    }


    /**
     * Bucket Class.
     * An inner class of StrHashTableCollisions that contains the key and value of the hash table elements,
     *  as well as a pointer to the next item in the bucket.
     * Contains a constructor for a Bucket object.
     * Contains a getKey() method to return the key .
     * Contains a getValue() method to return the value
     * Contains a getNext() method to set the next field
     * Contains a setNext() method to set the next field
     * Contains a setValue() method to set the value
     */
    private class Bucket {
        private String key;
        private String value;
        private Bucket next;


        /**
         * Bucket Constructor
         * Intialises a Bucket object to the key and value strings passed in, as well as the next pointer
         * @param k The key of the node, used to generate the index 
         * @param v The value of the node, the string to be stored in the table
         */
        public Bucket(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        /**
         * getKey.
         * Returns the private member variable key
         * @return String the private member variable key
         */
        public String getKey() {
            return key;
        }

        /**
         * getValue.
         * Returns the private member variable value
         * @return String the private member variable value
         */
        public String getValue() {
            return value;
        }

        /**
         * setValue.
         * Sets the private member variable value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * getNext()
         * Returns the private member variable value
         * @return Bucket the next item in the list
         */
        public Bucket getNext() {
            return next;
        }

        /**
         * setNext()
         * sets the next item in the list
         */
        public void setNext(Bucket next) {
            this.next = next;
        }
    }


    /**
     * insert.
     * Inserts a Bucket, consisting of a key value pair of strings and a pointer into the hashtable.
     * Rehashes the hashtable if the load factor is passed.
     * @param k The key of the node, used to generate the index
     * @param v The value of the node, the string to be stored in the table
     */
    public void insert(String k, String v) {
        //Check if the load factor has been reached
        if ((double) numFullEntries / size > load) {
            rehash();
            numRehashes++;
        }

        //get the index via the hash function
        int index = hashFunction(k);

        //check if the table is empty, make a new list if so
        if (hashtable[index] == null) {
            hashtable[index] = new ArrayList<>();
        }
        //add the values to the bucket
        hashtable[index].add(new Bucket(k, v));
        //increment full entries
        numFullEntries++;


    }



    /**
     * delete.
     * Deletes a Bucket from the hashtable that contains the key given as a parameter.
     * @param k The key of the Bucket to be deleted
     */
    public void delete(String k) {
        //get the index by calling the hash funstion
        int index = hashFunction(k);

        //if it isnt empty there
        if (hashtable[index] != null) {
            List<Bucket> bucketList = hashtable[index];
            if (bucketList != null) {
                //loop though the list 
                for (int i = 0; i < bucketList.size(); i++) {
                    Bucket current = bucketList.get(i);
                    //delete it from its position
                    if (current.getKey().equals(k)) {
                        if (i == 0) {
                            bucketList.remove(0);
                            if (bucketList.isEmpty()) {
                                // Set the bucket to null if it becomes empty
                                hashtable[index] = null; 
                            }
                        } else {
                            Bucket prev = bucketList.get(i - 1);
                            prev.setNext(current.getNext());
                            bucketList.remove(i);
                        }
                        // Decrement numFullEntries
                        numFullEntries--; 
                        return;
                    }
                }
            }
        }
    }


    /**
     * contains.
     * Checks if the hashtable contains the item with the key given as a parameter
     * @param k The key to be checked if contianed in the hashtable
     * @return Boolean true if the Node with the given key is found, false otherwise
     */
    public boolean contains(String k) {
        //Get the index from the hashFunction
        int index = hashFunction(k);

        //if the key appears return true
        if (hashtable[index] != null) {
            List<Bucket> bucketList = hashtable[index];
            for (Bucket bucket : bucketList) {
                if (bucket.getKey().equals(k)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * isEmpty.
     * Checks if the hashtable is empty
     * 
     * @return Boolean true if the hashtable is empty, false otherwise
     */
    public boolean isEmpty() {
        return numFullEntries == 0;
    }

    /**
     * size.
     * Gives the number of non-null entries that exist in the table
     * @return  Int of the number of non-null entries to the table
     */
    public int size() {
        return numFullEntries;
    }


    /**
     * dump.
     * Prints each key value pair of buckets in the hashtable to the standard output
     */
    public void dump() {
        for (int i = 0; i < hashtable.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(i).append(": ");
            List<Bucket> bucketList = hashtable[i];
            if (bucketList != null) {
                for (Bucket bucket : bucketList) {
                    sb.append("(").append(bucket.getKey()).append(",").append(bucket.getValue()).append(")  ->  ");
                }
            }
            sb.append("null");
            System.out.println(sb.toString());
        }
    }


    /**
     * get.
     * Gets the value of the entry with the same key as the parameter given
     * @param k The key of the entry, from which its value is to be returned.
     * @return String of the value of the Node with the given key
     */
    public String get(String k) {
        int index = hashFunction(k);

        if (hashtable[index] != null) {
            List<Bucket> bucketList = hashtable[index];
            for (Bucket bucket : bucketList) {
                if (bucket.getKey().equals(k)) {
                    return bucket.getValue();
                }
            }
        }
        return null;
    }


    /**
     * rehash.
     * Rehashes the hastable by doubling its size and reinserting the
     * key value pairs to the new table
     */
    public void rehash() {
        List<Bucket>[] hashtableCopy = hashtable;
        int newSize = size * 2;
        hashtable = new ArrayList[newSize];
        numFullEntries = 0;

        for (List<Bucket> bucketList : hashtableCopy) {
            if (bucketList != null) {
                for (Bucket bucket : bucketList) {
                    insert(bucket.getKey(), bucket.getValue());
                }
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
