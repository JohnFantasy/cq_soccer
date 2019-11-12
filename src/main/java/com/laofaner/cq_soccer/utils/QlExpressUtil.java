package com.laofaner.cq_soccer.utils;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * （1）打通了spring容器，通过扩展IExpressContext->QLExpressContext
 * 获取本地变量的时候，可以获取到spring的bean
 * （2）在runner初始化的时候，使用了函数映射功能：addFunctionOfServiceMethod
 * （3）在runner初始化的时候，使用了代码映射功能：addMacro
 */
public class QlExpressUtil {
	private static ExpressRunner runner;

	static {
		runner = new ExpressRunner();
	}

	/**
	 * @param statement 执行语句
	 * @param context   上下文
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object execute(String statement, Map<String, Object> context, List errorList)
			throws Exception {
		IExpressContext expressContext = new QLExpressContext(context);
		return runner.execute(statement, expressContext, errorList, true, false);
	}


	@SuppressWarnings("serial")
	private static class QLExpressContext extends HashMap<String, Object> implements
			IExpressContext<String, Object> {
		public QLExpressContext(Map<String, Object> aProperties) {
			super(aProperties);
		}

		/**
		 * 抽象方法：根据名称从属性列表中提取属性值
		 */
		public Object get(Object name) {
			Object result = null;
			result = super.get(name);
			try {
				if (result == null) {
					// 如果在 Spring 容器中包含 bean，则返回 String 的 Bean
					result = SpringBeanUtils.getBean(name.toString());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return result;
		}

		public Object put(String name, Object object) {
			return super.put(name, object);
		}

	}

}




