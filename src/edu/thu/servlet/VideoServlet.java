package edu.thu.servlet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.thu.bean.JSONResult;
import edu.thu.icomponent.IMessageComponent;
import edu.thu.icomponent.IVideoComponent;
import edu.thu.service.MessageService;
import edu.thu.service.VideoService;

public class VideoServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println(request.getRequestURL().toString());
		String action = request.getParameter("action");
		String repository = request.getParameter("repository");

		VideoService videoService = new VideoService();
		try {
			videoService.videoComponent = (IVideoComponent) Class.forName(
					"edu.thu.component." + repository.toLowerCase() + ".VideoComponent").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONResult result = new JSONResult();
		HashMap<String, String> paramMap = new HashMap<String, String>();
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			if (!key.equalsIgnoreCase("repository") && !key.equalsIgnoreCase("action")) {
				paramMap.put(key, request.getParameter(key));
			}
		}

		// using Java reflection to invoke component method
		try {
			Method method = videoService.getClass().getMethod(action, JSONResult.class, HashMap.class);
			method.invoke(videoService, result, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(result.buildJsonContent());
		BufferedWriter out = null;
		out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
		out.write(result.buildJsonContent());
		out.flush();
		out.close();
	}

}
