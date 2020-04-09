package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Smallholding;

@Service
@Transactional
public class UtilityService {

	public static final int LIMIT = 4;
	public static final int MAIN_PHOTO = 0;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private SmallholdingService smallholdingService;
	
	public UtilityService() {
		super();
	}

	
	public boolean hasAccessToExclusiveContent() {
		Collection<Smallholding> smallholdings;
		boolean result = false;
		Actor actor;
		
		actor = this.actorService.findByPrincipal();
				
		switch(actor.getUserAccount().getAuthorities().toString()){
			case "[RENTER]":
				smallholdings = this.smallholdingService.findSmallholdingsByActiveRentOut(actor.getId());
				
				result = smallholdings.size() > 0;
	
				break;
			case "[OWNER]":
				smallholdings = this.smallholdingService.findSmallholdingsByOwnerId(actor.getId());
				
				result = smallholdings.size() > 0;
				
				break;
			default:
				result = true;
		}
		
		return result;
	}
	
	public List<Integer> getPages(int totalPages) {
		List<Integer> results;
		
		results = new ArrayList<Integer>();
		
		if (totalPages > 0) {
			results = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
		}
		
		return results;
	}
	
	protected Date getCurrentMoment() {
		Date result;
		
		result = new Date(System.currentTimeMillis() - 1);
		
		return result;
	}
	
}
