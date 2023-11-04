package com.wanfeng.shop.user.controller;

import com.wanfeng.shop.user.model.request.AddressRequest;
import com.wanfeng.shop.user.service.AddressService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/address/v1")
public class AddressController {
    @Resource
    private AddressService addressService;
    @PostMapping("add")
    public JsonData add(@RequestBody AddressRequest addressRequest) {
        addressService.add(addressRequest);
        return JsonData.buildSuccess();
    }
    @GetMapping("/find{id}")
    public JsonData findAddressById(@PathVariable("id") Long id) {
        return addressService.findAddressById(id);
    }

    @DeleteMapping("/delete/{id}")
    public JsonData deleteAddressById(@PathVariable("id") Long id) {
        return addressService.deleteAddressById(id);
    }
    @GetMapping("/list")
    public JsonData findAddressList() {
        return addressService.findAddressList();
    }

}
