package edu.thu.icomponent;

import java.util.HashMap;

import edu.thu.bean.JSONResult;

public interface IActivityComponent {

	void activity(JSONResult xmlResult, HashMap<String, String> paramMap);

}
