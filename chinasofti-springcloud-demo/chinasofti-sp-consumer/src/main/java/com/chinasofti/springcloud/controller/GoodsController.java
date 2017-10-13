package com.chinasofti.springcloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.chinasofti.springcloud.entity.SpGoodsinfo;
import com.chinasofti.springcloud.utils.JsonUtils;

@RestController
/**
 * goods控制器
 * 访问方式启动本地eureka服务器和服务提供者，通过ip+port加上/goods/id
 * @author husong
 *
 */
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${user.goodServicepath}")
	private String goodServicepath;


	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	@GetMapping("/select/{id}")
	public String goodsFindById(@PathVariable Long id) {
		
		
		SpGoodsinfo spGoodsinfo = this.restTemplate.getForObject(this.goodServicepath + "goods/select/" + id, SpGoodsinfo.class);
		
		return JsonUtils.objectToGsonString(spGoodsinfo);
	}
	
	/**
	 * 全部查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/list")
	@ResponseBody
	public List<SpGoodsinfo> findAll(){
		
		return this.restTemplate.getForObject(this.goodServicepath + "goods/list", List.class);
	}
	
	
	/**
	 * 商品添加
	 * @param spGoodsinfo
	 * @return
	 */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	public ResponseEntity<String> goodsAdd(SpGoodsinfo spGoodsinfo){
		
		LinkedMultiValueMap<String,String> map = new LinkedMultiValueMap<String,String>();
		
		String goodsjson = JsonUtils.objectToGsonString(spGoodsinfo);
		
		map.add("spGoodsinfo", goodsjson);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<LinkedMultiValueMap<String,String>> he = new HttpEntity<LinkedMultiValueMap<String,String>>(map,headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(this.goodServicepath + "goods/add",he,String.class);

		
		return response;
	}
	
	/**
	 * 通过ID删除
	 * @return
	 */
	@RequestMapping(value = "/delete/{ids}")
	public String goodsDeleteById(@PathVariable String ids){
		
		System.out.println(ids);
		
		return this.restTemplate.getForObject(this.goodServicepath + "goods/delete/" + ids, String.class);
		
		
	}
	
	
	
	
	/**
	 * 返回商品界面
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView getview() {
		return  new ModelAndView("/goods/goods");
	}
	
	

}
