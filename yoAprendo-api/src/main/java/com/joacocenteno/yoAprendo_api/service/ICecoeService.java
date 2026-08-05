package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.CecoeRequest;
import com.joacocenteno.yoAprendo_api.dto.CecoeResponse;
import com.joacocenteno.yoAprendo_api.dto.UserResponse;
import com.joacocenteno.yoAprendo_api.model.UserRol;

public interface ICecoeService {
    public List<CecoeResponse> getAllCecoe();
    public CecoeResponse getCecoeById(Long cecoe_id);
    public CecoeResponse createCecoe(CecoeRequest cecoe);
    public CecoeResponse editCecoe(Long cecoe_id, CecoeRequest cecoe);
    public void toggleActiveCecoe(Long cecoe_id);
    public List<UserResponse> getUsersByCecoe(Long cecoeId);
    public List<UserResponse> getUsersByCecoeAndRole(Long cecoeId, UserRol role);
}
