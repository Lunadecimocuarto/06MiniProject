package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.service.product.ProductService;
import com.model2.mvc.service.domain.Product;

@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	///Constructor
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	//@Value를 통해 속성을 클래스 속성에 주입할 수 있음
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception{
		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct(@ModelAttribute("product") Product product) throws Exception{
		
		System.out.println("/addProduct.do");
		
		product.setManuDate(product.getManuDate().replace("-",""));
		productService.addProduct(product);
			
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int ProdNo, @RequestParam("menu") String menu, Model model) throws Exception{
		
		System.out.println("/getProduct.do");
		
		Product product = productService.getProduct(ProdNo);
		model.addAttribute("product",product);
		
		if(menu.equals("manage")) {
			return "forward:/product/updateProductView.jsp";
		}else {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView(@RequestParam("prodNo") int ProdNo, Model model) throws Exception{
		
		System.out.println("/updateProductView.do");
		Product product = productService.getProduct(ProdNo);
		
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product, Model model, HttpSession session) 
		throws Exception{
		
		System.out.println("/updateProduct.do");
		product.setManuDate(product.getManuDate().replace("-",""));
		productService.updateProduct(product);
		int seesionId = ((Product)session.getAttribute("product")).getProdNo();
		
		if(seesionId==product.getProdNo()) {
			session.setAttribute("product", product);
		}
		return "redirect:/product/updateProduct?prodNo="+product.getProdNo();
	}
		
	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search, Model model, HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage()==0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page(search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		 
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";
	}

}
