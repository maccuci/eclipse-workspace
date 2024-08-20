package com.fuzion.core.master.server.loadbalancer;

import com.fuzion.core.master.server.loadbalancer.element.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {
	
	public T next();

}
