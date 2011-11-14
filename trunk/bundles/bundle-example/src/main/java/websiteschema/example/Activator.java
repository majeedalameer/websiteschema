package websiteschema.example;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import websiteschema.fb.core.Application;
import websiteschema.fb.core.ApplicationService;
import websiteschema.fb.core.RuntimeContext;

public class Activator implements BundleActivator, ServiceListener {

    ApplicationService service = null;
    BundleContext bundleContext = null;

    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        ServiceReference[] refs = bundleContext.getServiceReferences(ApplicationService.class.getName(), "(ApplicationService=*)");
        if (null != refs && refs.length > 0) {
            System.out.println("ApplicationManager started already!");
            service = (ApplicationService) bundleContext.getService(refs[0]);
            addApplication(service);
        } else {
            bundleContext.addServiceListener(this, "(objectClass=" + ApplicationService.class.getName() + ")");
            System.out.println("wait for register event of ApplicationService.");
        }
    }

    public void addApplication(ApplicationService service) {
        Application app = new Application();
        RuntimeContext runtimeContext = app.getContext();
        runtimeContext.loadConfigure(Activator.class.getClassLoader().getResourceAsStream("fb/test.app"));
        service.startup(app);
    }

    public void stop(BundleContext context) {
        // NOTE: The service is automatically released.
        System.out.println("Good bye!");
    }

    public void serviceChanged(ServiceEvent se) {
        if (se.getType() == ServiceEvent.REGISTERED) {
            if (service == null) {
                // Get a reference to the service object.
                ServiceReference ref = se.getServiceReference();
                service = (ApplicationService) bundleContext.getService(ref);
                if (null != service) {
                    System.out.println("ApplicationManager already started!");
                    addApplication(service);
                }
            }
        }
    }
}
