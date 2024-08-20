package me.tony.school.products;

import java.util.ArrayList;
import java.util.List;

import me.tony.school.products.types.RefigeranteProduct;
import me.tony.school.products.types.TortaDeMacaProduct;

public class Main {
	
	static final List<Product> products = new ArrayList<>();
	
	private static void listProducts() {
		products.add(new RefigeranteProduct());
		products.add(new TortaDeMacaProduct());
	}
	
	public static void main(String[] args) {
		listProducts();
		
		for(Product product : products) {
			String productName = product.getName().trim();
			int productId = product.getId();
			double productValue = product.getValue();
			StringBuilder stringBuilder = new StringBuilder();
			
			for(int i = 0; i < productName.length(); i++) {
				if(i > 15) {
					stringBuilder.append("...");
					break;
				}
				stringBuilder.append(productName.charAt(i));
			}
			
			String newProductName = stringBuilder.toString();
			String newValue = String.valueOf(productValue).replace(".", ",");
			String description = productId + " | " + newProductName + " R$: " + newValue + "\n";
			
			System.out.print(description);
		}
	}

}
