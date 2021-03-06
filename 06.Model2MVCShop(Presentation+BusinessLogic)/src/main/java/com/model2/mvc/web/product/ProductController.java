package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.service.product.ProductService;
import com.model2.mvc.service.domain.Product;

@Controller
public class ProductController {

	/// Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	/// Constructor
	public ProductController() {
		System.out.println(this.getClass());
	}

	// @Value를 통해 속성을 클래스 속성에 주입할 수 있음
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	int pageSize;

	@RequestMapping("/addProductView.do")
	// public String addProductView() throws Exception{
	public ModelAndView addProductView() throws Exception {
		System.out.println("/addProductView.do");

		// return "redirect:/product/addProductView.jsp";
		return new ModelAndView("redirect:/product/addProductView.jsp");
	}

	@RequestMapping("/addProduct.do")
	// public String addProduct(@ModelAttribute("product") Product product) throws
	// Exception{
	public ModelAndView addProduct(@ModelAttribute("product") Product product) throws Exception {

		System.out.println("/addProduct.do");

		product.setManuDate(product.getManuDate().replace("-", ""));
		productService.addProduct(product);

		// return "forward:/product/addProduct.jsp";
		return new ModelAndView("/product/addProduct.jsp");
	}

	@RequestMapping("/getProduct.do")
	// public String getProduct(@RequestParam("prodNo") int prodNo,
	// @RequestParam("menu") String menu, Model model, HttpServletRequest
	// request,HttpServletResponse response) throws Exception{
	public ModelAndView getProduct(@RequestParam("prodNo") int prodNo, @RequestParam("menu") String menu, Model model,
			/* @CookieValue(value="history", required=false) Cookie cookie, */
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println("/getProduct.do");

		Product product = productService.getProduct(prodNo);
		model.addAttribute("product", product);

		
		  String history = null; 
		  Cookie[] cookies = request.getCookies(); 
		  if(cookies != null && cookies.length>0) { 
			  for(int i=0 ; i<cookies.length; i++) { 
				  Cookie cookie=cookies[i]; 
				  if(cookie.getName().equals("history")) { 
					  history =  cookie.getValue() ; 
					  } 
				  } 
			  } 
		  history += ","+product.getProdNo(); 
		  Cookie cookie=new Cookie("history", history);
		  response.addCookie(cookie);
		 

		/*
		 * String history = null; Cookie[] cookies = request.getCookies();
		 * if(cookie.getName().equals("history")) { history=cookie.getValue(); }
		 */

		if (menu.equals("manage")) {
			// return "forward:/product/updateProductView.jsp";
			return new ModelAndView("/product/updateProductView.jsp");
		} else {
			// return "forward:/product/getProduct.jsp";
			return new ModelAndView("/product/getProduct.jsp");
		}
	}

	@RequestMapping("/updateProductView.do")
	// public String updateProductView(@RequestParam("prodNo") int prodNo, Model
	// model) throws Exception{
	public ModelAndView updateProductView(@RequestParam("prodNo") int prodNo, Model model) throws Exception {

		System.out.println("/updateProductView.do");
		Product product = productService.getProduct(prodNo);

		model.addAttribute("product", product);

		// return "forward:/product/getProduct.jsp";
		return new ModelAndView("/product/getProduct.jsp");
	}

	@RequestMapping("/updateProduct.do")
	// public String updateProduct(@ModelAttribute("product") Product product, Model
	// model) throws Exception{
	public ModelAndView updateProduct(@ModelAttribute("product") Product product, Model model) throws Exception {

		System.out.println("/updateProduct.do");
		product.setManuDate(product.getManuDate().replace("-", ""));
		productService.updateProduct(product);

		product = productService.getProduct(product.getProdNo());

		// return "forward:/product/updateProduct.jsp";
		return new ModelAndView("/product/updateProduct.jsp");
	}

	@RequestMapping("/listProduct.do")
	// public String listProduct(@ModelAttribute("search") Search search, Model
	// model) throws Exception{
	public ModelAndView listProduct(@ModelAttribute("search") Search search, Model model) throws Exception {

		System.out.println("/listProduct.do");

		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);

		Map<String, Object> map = productService.getProductList(search);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);
		System.out.println(resultPage);

		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		// return "forward:/product/listProduct.jsp";
		return new ModelAndView("/product/listProduct.jsp");
	}

}
