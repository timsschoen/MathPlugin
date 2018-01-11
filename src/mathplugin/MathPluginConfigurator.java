package mathplugin;

import eu.mihosoft.vrl.system.InitPluginAPI;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginIdentifier;
import eu.mihosoft.vrl.system.VPluginAPI;
import eu.mihosoft.vrl.system.VPluginConfigurator;

/**
*
* @author Michael Hoffer <info@michaelhoffer.de>
*/

public class MathPluginConfigurator extends VPluginConfigurator{

public MathPluginConfigurator() {
    //specify the plugin name and version
   setIdentifier(new PluginIdentifier("MathPlugin", "3.1415"));

   // describe the plugin
   setDescription("Vector and Matrix Math");

   // copyright info
   setCopyrightInfo("MathPlugin",
           "(c) Tim Schön",
           "www.you.com", "License Name", "License Text...");
}

@Override
public void register(PluginAPI api) {

   // register plugin with canvas
   if (api instanceof VPluginAPI) {
       VPluginAPI vapi = (VPluginAPI) api;
       vapi.addComponent(Matrix.class);
       vapi.addComponent(Vector.class);
       }
}

@Override
public void unregister(PluginAPI api) {
   // nothing to unregister
}

@Override
public void init(InitPluginAPI iApi) {
   // nothing to init
  }
}