package algorithm;

import java.util.ArrayList;
import java.util.List;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(removeCustomerTagAssoc("key", "value", 1990, "type", "uu1"));

	}
	
	 public static StringBuilder removeCustomerTagAssoc(String tagKey, String tagValue, int period, String targetType, String correlationAssetId)
	   {
		  StringBuilder sql = new StringBuilder();
		  List<String> columns = new ArrayList<>();
		  columns.add("tagKey");
		  columns.add("tagValue");
		  columns.add("period");
		  columns.add("targetType");
		  columns.add("correlationAssetId");
		  sql.append("delete from customer_tag_association").append(" ").append(generateWhereCondition(columns));
		  
		  return sql;
	   }
	   private static String generateWhereCondition(List<String> columns){
		   StringBuilder where = new StringBuilder("where ");
		   int position = 1;
		   for(String column : columns){
			   where.append(column).append("=?").append(position++);
			   if(position <= columns.size()){
				   where.append(" and ");
			   }
		   }
		   return where.toString();
		   
	   }

}
