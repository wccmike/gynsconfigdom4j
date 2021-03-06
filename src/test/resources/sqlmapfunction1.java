package com.csii.mca.service.ent.edraft.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csii.mca.datamodel.ent.edraft.MCBillHisInfoQueryPRequest;
import com.csii.mca.datamodel.ent.edraft.MCBillHisInfoQueryPResponse;
import com.csii.mca.service.MCQueryService;
import com.csii.mca.service.ent.edraft.MCBillHisInfoQueryPService;


public class MCBillHisInfoQueryPServiceImpl
		extends
		MCQueryService<MCBillHisInfoQueryPRequest, MCBillHisInfoQueryPResponse>
		implements MCBillHisInfoQueryPService {

	public MCBillHisInfoQueryPResponse query(
			MCBillHisInfoQueryPRequest request) {
		MCBillHisInfoQueryPResponse mcBillHisInfoQueryPResponse = new MCBillHisInfoQueryPResponse();

		List list = new ArrayList();
		Map map = new HashMap();
		if(request.getStdrealamt1()!=null&&!"".equals(request.getStdrealamt1())){
			BigDecimal Stdrealamt1=new BigDecimal(request.getStdrealamt1().toString());
			map.put("Stdrealamt1", request.getStdrealamt1());
		}
		if(request.getStdrealamt2()!=null&&!"".equals(request.getStdrealamt2())){
			BigDecimal Stdrealamt2=new BigDecimal(request.getStdrealamt2());
			map.put("Stdrealamt2", request.getStdrealamt2());
		}
		if(request.getStdbillnum()!=null&&!"".equals(request.getStdbillnum())){
			map.put("Stdbillnum", request.getStdbillnum());
		}
		map.put("BeginDate", request.getBeginDate());
		map.put("EndDate", request.getEndDate());
		list = this.sqlMap.queryForList("balabala.loadJEdraft1", map);
		map.put("RequestCifSeq", getRequestContext().getRequestCifSeq());  //当前客户顺序号
		map.put("RequestUserSeq", getRequestContext().getRequestUserSeq());//当前操作员顺序号
		list = this.sqlMap.queryForList("timi.loadJEdraft1", map);
		mcBillHisInfoQueryPResponse.setList(list);
list = this.sqlMap.queryForList("balabala.loadJEdraft1", map);
		return prepareResponse(mcBillHisInfoQueryPResponse,this.getResponseContext());
	}
}
