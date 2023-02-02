package com.apps.pims.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apps.pims.dto.SearchOrder;
import com.apps.pims.entity.Order;
import com.apps.pims.entity.Products;
import com.apps.pims.entity.Supplier;
import com.apps.pims.service.OrderService;
import com.apps.pims.service.ProductService;
import com.apps.pims.service.SupplierService;
import com.apps.pims.util.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/products")
public class ProductsController {

	private OrderService orderService;

	private SupplierService supplierService;

	private ProductService productService;

	public ProductsController(OrderService orderService, SupplierService supplierService,
			ProductService productService) {
		super();
		this.orderService = orderService;
		this.supplierService = supplierService;
		this.productService = productService;
	}

	// handler method to handle list Orders and return mode and view
	@GetMapping()
	public String listProductss(Model model) throws IOException {

		int pageSize = 10;
		int pageNo = 1;
		List<Products> productsList = new ArrayList<>();
		Page<Products> products = productService.getAllProducts(pageNo, pageSize);
		if (products != null) {
			productsList = products.getContent();
			model.addAttribute("totalPages", products.getTotalPages());
			model.addAttribute("totalRecords", products.getTotalElements());
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("orders", productsList);
		} else {
			model.addAttribute("totalPages", 0);
			model.addAttribute("totalRecords", 0);
			model.addAttribute("currentPage", 0);
			model.addAttribute("orders", productsList);
		}

		return "orders";
	}

	@GetMapping("/productsList/{orderId}")
	public String allProducts(@PathVariable Long orderId,Model model) throws IOException {

		int pageSize = 10;
		int pageNo = 1;
		List<Products> productsNewList = new ArrayList<>();
		List<Products> productsList = new ArrayList<>();
		Page<Products> products = productService.getAllProductsByOrderId(orderId,pageNo, pageSize);
		if (products != null) {
			productsList = products.getContent();

			for (Products product : productsList) {
				if (product.getPoImageData() != null) {
					product.setPoImageBase64(
							Base64.getEncoder().encodeToString(ImageUtils.decompressImage(product.getPoImageData())));
				}

				productsNewList.add(product);

				model.addAttribute("totalPages", products.getTotalPages());
				model.addAttribute("totalRecords", products.getTotalElements());
				model.addAttribute("currentPage", pageNo);
				model.addAttribute("products", productsNewList);
			}
		} else {
			model.addAttribute("totalPages", 0);
			model.addAttribute("totalRecords", 0);
			model.addAttribute("currentPage", 0);
			model.addAttribute("products", productsNewList);
		}

		return "productsList";
	}

	@GetMapping("/{id}")
	public String getProductById(@PathVariable Long id, Model model) throws IOException {
		Optional<Products> product = productService.getProductById(id);
		model.addAttribute("product", product.get());
		return "product";

	}

	@GetMapping("/quickView/{id}")
	public String orderQuickView(@PathVariable Long id, Model model) throws IOException {
		Order order = orderService.getOrderById(id);
		Supplier supplier = null;
		
		Optional<Products> productData = productService.getProductById(id);
		Products product = null;
		if (productData != null) {
			product=productData.get();
		}
		if (order != null) {
			supplier = supplierService.getSupplierById(order.getSupplierId());
		}
		model.addAttribute("order", order);
		model.addAttribute("supplier", supplier);

		String orderReceiptImageBase64 = Base64.getEncoder()
				.encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData()));
		model.addAttribute("orderReceiptImageBase64", orderReceiptImageBase64);

		String orderProductImageBase64 = Base64.getEncoder()
				.encodeToString(ImageUtils.decompressImage(product.getPoImageData()));
		model.addAttribute("orderProductImageBase64", orderProductImageBase64);

		return "orderQuickView";

	}
	
	@GetMapping("/new/products/{orderId}")
	public String createProductForm(@PathVariable Long orderId,Model model) throws IOException {
		log.info("createSupplierForm()");
		// create Supplier object to hold Supplier form data
		Order order=orderService.getOrderById(orderId);
		Products product = new Products();
		product.setOrderId(orderId);
		model.addAttribute("orderId",orderId);
		model.addAttribute("order",order);
		model.addAttribute("product", product);
		return "create_product";

	}

	@PostMapping("/addProduct")
	public String saveProduct(@ModelAttribute("product") Products product,Model model) throws IOException {
		Products productCreated = productService.saveProduct(product);
		log.info("product Created: "+productCreated);
		
		List<Products> productsNewList = new ArrayList<>();
		List<Products> products = productService.getAllProductsByOrderId(productCreated.getOrderId());

		if (products != null) {
			
			for (Products productO : products) {
				if (productO.getPoImageData() != null) {
					productO.setPoImageBase64(Base64.getEncoder().encodeToString(ImageUtils.decompressImage(product.getPoImageData())));
				}
				productsNewList.add(productO);			
			}
			model.addAttribute("productsList", productsNewList);
		} else {
		
			model.addAttribute("productsList", null);
		}
		model.addAttribute("orderId",product.getOrderId());
		model.addAttribute("product", productCreated);
		
		Order order = orderService.getOrderById(product.getOrderId());
		model.addAttribute("order",order);
		
		Supplier supplier = null;
		if(order!=null) {
			supplier=supplierService.getSupplierById(order.getSupplierId());
		}
		model.addAttribute("supplier", supplier);
		
		String orderReceiptImageBase64 = Base64.getEncoder()
				.encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData()));
		model.addAttribute("orderReceiptImageBase64", orderReceiptImageBase64);
		model.addAttribute("message", "Product Details added successfully for the purchase order");
		return "addPOTab";
	}

	@GetMapping("/edit/{id}")
	public String editOrderForm(@PathVariable Long id, Model model) throws IOException {
		Optional<Products> productData = productService.getProductById(id);
		Products product = null;
		if (productData != null) {
			product = productData.get();
			if (product != null && product.getPoImageData() != null) {
				log.info("Product Image Name from db: " + product.getPoImageName());
				// log.info("Product Image Data from db: " + order.getPoImageData());
				product.setPoImageBase64(Base64.getEncoder().encodeToString(ImageUtils.decompressImage(product.getPoImageData())));
			}
		}
		model.addAttribute("product", product);
		return "edit_product";
	}
	
	@PostMapping("/updateProduct/{id}")
	public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Products product,Model model) throws IOException {
		Products productCreated = productService.saveProduct(product);
		log.info("product Created: "+productCreated);
		
		List<Products> productsNewList = new ArrayList<>();
		List<Products> products = productService.getAllProductsByOrderId(product.getOrderId());

		if (products != null) {
			
			for (Products productO : products) {
				if (productO.getPoImageData() != null) {
					productO.setPoImageBase64(Base64.getEncoder().encodeToString(ImageUtils.decompressImage(product.getPoImageData())));
				}
				productsNewList.add(productO);			
			}
			model.addAttribute("productsList", productsNewList);
		} else {
		
			model.addAttribute("productsList", null);
		}
		
		model.addAttribute("orderId",product.getOrderId());
		model.addAttribute("product", productCreated);
		
		Order order = orderService.getOrderById(product.getOrderId());
		model.addAttribute("order",order);
		
		Supplier supplier = null;
		if(order!=null) {
			supplier=supplierService.getSupplierById(order.getSupplierId());
		}
		model.addAttribute("supplier", supplier);
		
		String orderReceiptImageBase64 = Base64.getEncoder()
				.encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData()));
		model.addAttribute("orderReceiptImageBase64", orderReceiptImageBase64);
		
		return "viewPOTab";
	}

	@PostMapping("/{id}")
	public String updateOrder(@PathVariable Long id, @ModelAttribute("order") Order order, Model model)
			throws IOException {
		log.info("save updated Order object");
		order.setId(id);
		orderService.updateOrder(order);
		return "redirect:/order/ordersList";
	}

	// handler method to handle delete Order request

	@GetMapping("/delete/{id}")
	public String deleteOrder(@PathVariable Long id) {
		orderService.deleteOrderById(id);
		return "redirect:/order";
	}

	@PostMapping("/searchOrders")
	public String searchOrders(@ModelAttribute("searchOrder") SearchOrder searchOrder, Model model) throws IOException {

		log.info("Search Order details: " + searchOrder);
		int pageSize = 10;
		int pageNo = 1;
		Page<Order> ordr = orderService.getAllOrders(1, pageSize);
		List<Order> orderList = ordr.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", ordr.getTotalPages());
		model.addAttribute("totalRecords", ordr.getTotalElements());

		model.addAttribute("order", orderList);
		return "orders";
	}

	@GetMapping("/image/download/{id}")
	public void downloadProductImage(@PathVariable Long id, Model model, HttpServletResponse response)
			throws IOException {
		Optional<Products> product = productService.getProductById(id);
		log.info("downloadOrderReceipt() ...order id: " + id);
		if (product != null) {
			response.setContentType("image/jpeg, image/jpg,,image/JPG,image/JPEG, image/png, image/gif, image/pdf");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename = " + product.get().getPoImageName();
			response.setHeader(headerKey, headerValue);
			ServletOutputStream outputStream = response.getOutputStream();
			if (product.get().getPoImageData() != null) {
				byte[] image = ImageUtils.decompressImage(product.get().getPoImageData());
				outputStream.write(image);

			} else {
				log.warn("image data is null");
			}
			outputStream.close();
		}
	}

	@GetMapping("/image/preview/{id}")
	public String showProductImage(@PathVariable Long id, Model model) throws ServletException, IOException {
		log.info("showOrderImage() ...order id: " + id);
		Optional<Products> product = productService.getProductById(id);

		String orderProductImageBase64 = Base64.getEncoder()
				.encodeToString(ImageUtils.decompressImage(product.get().getPoImageData()));
		model.addAttribute("orderProductImageBase64", orderProductImageBase64);

		return "productImagePreview";
	}

	@GetMapping("/page/{pageNo}")
	public String displayProducts(@PathVariable(value = "pageNo") int pageNo, Model model) throws IOException {

		int pageSize = 10;
		Page<Products> products = productService.getAllProducts(pageNo, pageSize);
		List<Products> productsList = products.getContent();

		model.addAttribute("totalPages", products.getTotalPages());
		model.addAttribute("totalRecords", products.getTotalElements());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("orders", productsList);

		model.addAttribute("products", productsList);
		return "productsList";

	}

}
