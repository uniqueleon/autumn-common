package org.aztec.autumn.common.utils.job;

public class JobTaskLoader extends ClassLoader {

  private String remoteHost;
  
  public JobTaskLoader(String remoteHost) {
    // TODO Auto-generated constructor stub
    this.remoteHost = remoteHost;
  }

  public JobTaskLoader(ClassLoader parent) {
    super(parent);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    try {
      Class loadedCls = super.loadClass(name);
      return loadedCls;
    } catch (ClassNotFoundException e) {
      /*BundleContext context = CoreUtilActivator.getContext();
      if(context != null){
        ServiceReference sr = CoreUtilActivator.getContext().getServiceReference(name);
        if(sr != null){
          return CoreUtilActivator.getContext().getService(sr).getClass();
        }
        else{
          
        }
      }*/
    }
    return super.loadClass(name);
  }
  
//  private Class<?> loadClassFromRemote(){
//    super.defineClass(name, b, off, len)
//  }

}
