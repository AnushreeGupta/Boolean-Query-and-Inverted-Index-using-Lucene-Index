import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class LuceneIndex {
	
	
	public static final String inputfile = "/home/anushree/workspace/IRProject2/input";

	public static void main(String[] args) throws IOException {
		
		final String path = args[0];
		
		LuceneIndex L = new LuceneIndex();
		L.BuildInvertedIndex(path);

	}
	
	void BuildInvertedIndex(String path) throws IOException
	{
		 
		 
		 FileSystem fs = FileSystems.getDefault();
		 Path path1 = fs.getPath(path);  
		 IndexReader reader = DirectoryReader.open(FSDirectory.open(path1));
		 
		 //Verifying the total documents retrieved
		 int n = reader.maxDoc();
		 System.out.println("Total Documents : " +n);
		 
         
         //Declaring structures for storing the postings list
		 LinkedList<Integer> post_list =  new LinkedList<Integer>();
		 HashMap<String, LinkedList<Integer>> final_map = new HashMap<String, LinkedList<Integer>>();  
		 

         //To get all the fields present in the Lucene index
         Fields f = MultiFields.getFields(reader);
         PostingsEnum postings;
         
         int i = 0;
         
         // Iterate for all the fields present in the Lucene Index
         for (String field : f)	
         { 
        	 int freq =0;
        	 String S = null;
        	 int count = 0;
        	 String fields = field.toString();
        	 
        	 //For filtering out only the text fields and iterating further on them
        	 if(field.contains("text_"))	 
        	 { 
        		 Terms terms = f.terms(field);
        		 TermsEnum termsEnum = terms.iterator();
        		 
        		 BytesRef t;
        		 
        		 //Iterating for all Terms
        		 while((t = termsEnum.next()) != null )
        		 { 
        			 
 	                 freq = termsEnum.docFreq();
 	                 S = t.utf8ToString();
 	                 postings = MultiFields.getTermDocsEnum(reader, field, t);
 	                 
 	                 post_list.clear();
 	                 
 	                 //Iterating for all Document IDs
        			 while(postings.nextDoc() != PostingsEnum.NO_MORE_DOCS)
        		 	 {
        				 count++;
        				 post_list.add(postings.docID());
        	   
        		 	 }
        			 
        			 //Add the term as key and postings list into hash map
        			 final_map.put(S, post_list);
        			 i++;
        			 
        			 //System.out.println("field : " +fields+ " Terms : " +S+ " DocIDs : " +post_list);
        	                
        		 }
        		 
        		 System.out.println(field+ " Terms : " +count);
        		        		 
        	 }
        	 
         }
         
         System.out.println("Completed & number of entries are " +i);
         
		 reader.close();
		 
	}

	
	void getPostings(File f) throws IOException
	{
		
		//FileReader file = new FileReader(f);
		 FileReader file = new FileReader(inputfile); 
		
		file.close();
	}
	

}
