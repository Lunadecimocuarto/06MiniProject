package com.model2.mvc.service.product.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.service.product.ProductService;
import com.model2.mvc.service.domain.Product;
import com.sun.media.sound.DLSSoundbank;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:config/context-common.xml",
								"classpath:config/context-aspect.xml",
								"classpath:config/context-mybatis.xml",
								"classpath:config/context-transaction.xml"})


public class ProductServiceTest {

	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Test
	public void testAddProduct() throws Exception{
		
		Product product=new Product();
		product.setProdName("testProdName");
		product.setProdDetail("testProdDetail");
		product.setManuDate("20211013");
		product.setPrice(30000);
		product.setFileName("testFileName");
		
		productService.addProduct(product);
		
		System.out.println(product);
		
		Assert.assertEquals("testProdName", product.getProdName());
		Assert.assertEquals("tesFileName", product.getFileName());
		Assert.assertEquals("testProdDetail", product.getProdDetail());
		Assert.assertEquals(30000, product.getPrice());;

	}
	
	//@Test
	public void testGetProduct() throws Exception{
		Product product = new Product();
		
		product = productService.getProduct(10001);
		
		Assert.assertEquals("testProdName", product.getProdName());
		Assert.assertEquals("testFileName", product.getFileName());
		Assert.assertEquals("testProdDetail", product.getProdDetail());
		Assert.assertEquals(30000, product.getPrice());	
	}
	
	//@Test
	public void testUpdateProduct() throws Exception{
		
		Product product = productService.getProduct(10001);
		Assert.assertNotNull(product);
		
		Assert.assertEquals("testProdName", product.getProdNo());
		Assert.assertEquals("testFileName", product.getFileName());
		Assert.assertEquals("testProdDetail", product.getProdDetail());
		Assert.assertEquals(30000, product.getPrice());
		
		product.setFileName("change");
		product.setProdDetail("changeDetail");
		product.setFileName("changeFileName");
		product.setPrice(530000);
		
		productService.updateProduct(product);
		
		product = productService.getProduct(10001);
		
		Assert.assertEquals("change", product.getProdName());
		Assert.assertEquals("changeFileName", product.getFileName());
		Assert.assertEquals("changeDetail", product.getProdDetail());
		Assert.assertEquals(530000, product.getPrice());
	}
	
	//@Test
	public void testGetProductListAll() throws Exception{
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		Map<String, Object> map = productService.getProductList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(3,list.size());
		
		System.out.println(list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
		System.out.println("=======================================");
		
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("0");
		search.setSearchKeyword("");
		map = productService.getProductList(search);
		
		list = (List<Object>)map.get("list");
		Assert.assertEquals(3, list.size());
		
		totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);	
	}
	
	//@Test
	public void testGetProductListByProductId() throws Exception{
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("0");
		search.setSearchKeyword("");
		Map<String,Object> map = productService.getProductList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(1, list.size());
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
		System.out.println("===========================================");
		
		search.setSearchCondition("0");
		search.setSearchKeyword(""+System.currentTimeMillis());
		map = productService.getProductList(search);
		
		list = (List<Object>)map.get("list");
		Assert.assertEquals(0, list.size());
		
		totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
	}
	
	//@Test
	public void testGetProductListByProductName() throws Exception{
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("1");
		search.setSearchKeyword("");
		Map<String,Object> map = productService.getProductList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(3, list.size());
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
		System.out.println("=============================");
		
		search.setSearchCondition("1");
		search.setSearchKeyword(""+System.currentTimeMillis());
		map = productService.getProductList(search);
		
		list = (List<Object>)map.get("list");
		Assert.assertEquals(0, list.size());
		
		totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
				
	}
	
	
}