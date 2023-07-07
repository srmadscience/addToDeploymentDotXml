
# addtodeploymentdotxml #

This utility adds a fragment of XML to a deployment.xml file for a running VoltDB database.

Is is *not* intended to be used on a production system.

It works by looking for a pattern - such as the word 'deployment'. It assumes the pattern is an XMl end tag and injects the new content in just before it. It then calls UpdateCatalog.

## Example ##

Imagine we want to add an export connector to a brand new VoltDB database. Our extra XML is in a file called 'newbit.xml' and looks like this 


    <export>
        <configuration target="USER_TRANSACTIONS" enabled="true" type="file">
            <property name="type">csv</property>
            <property name="nonce">user_transactions</property>
            <property name="outdir">/Users/drolfe/Desktop</property>
        </configuration>
    </export>

The deployment.xml looks like this:


    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <deployment>
        <cluster hostcount="1"/>
        <partition-detection/>
        <heartbeat/>
        <ssl/>
        <httpd enabled="true">
            <jsonapi enabled="true"/>
        </httpd>
        <snapshot enabled="false"/>
        <commandlog enabled="false">
            <frequency/>
        </commandlog>
        <systemsettings>
            <temptables/>
            <snapshot/>
            <elastic/>
            <query/>
            <procedure/>
            <resourcemonitor>
                <memorylimit/>
            </resourcemonitor>
            <flushinterval>
                <dr/>
                <export/>
            </flushinterval>
        </systemsettings>
        <security/>
    </deployment>


We call our jar file and ask it to add newbit.xml just before '</deployment>':

    Davids-MacBook-Pro-8:jars drolfe$ java -jar addtodeploymentdotxml.jar  localhost deployment /Users/drolfe/Desktop/EclipseWorkspace/xtra.txt
    2020-11-20 09:20:59:Params: [localhost, deployment, /Users/drolfe/Desktop/EclipseWorkspace/xtra.txt]
    2020-11-20 09:20:59:Logging into VoltDB
    WARNING: An illegal reflective access operation has occurred
    WARNING: Illegal reflective access by io.netty_voltpatches.NinjaKeySet (file:/Users/drolfe/Desktop/EclipseWorkspace/addtodeploymentdotxml/jars/addtodeploymentdotxml.jar) to field sun.nio.ch.SelectorImpl.selectedKeys
    WARNING: Please consider reporting this to the maintainers of io.netty_voltpatches.NinjaKeySet
    WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
    WARNING: All illegal access operations will be denied in a future release
    2020-11-20 09:20:59:Connect to localhost...
    2020-11-20 09:20:59:Deployment file is /Users/drolfe/voltdbroot/config/deployment.xml
    2020-11-20 09:20:59:OLD:<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    2020-11-20 09:20:59:OLD:<deployment>
    2020-11-20 09:20:59:OLD:    <cluster hostcount="1"/>
    2020-11-20 09:20:59:OLD:    <partition-detection/>
    2020-11-20 09:20:59:OLD:    <heartbeat/>
    2020-11-20 09:20:59:OLD:    <ssl/>
    2020-11-20 09:20:59:OLD:    <httpd enabled="true">
    2020-11-20 09:20:59:OLD:        <jsonapi enabled="true"/>
    2020-11-20 09:20:59:OLD:    </httpd>
    2020-11-20 09:20:59:OLD:    <snapshot enabled="false"/>
    2020-11-20 09:20:59:OLD:    <commandlog enabled="false">
    2020-11-20 09:20:59:OLD:        <frequency/>
    2020-11-20 09:20:59:OLD:    </commandlog>
    2020-11-20 09:20:59:OLD:    <systemsettings>
    2020-11-20 09:20:59:OLD:        <temptables/>
    2020-11-20 09:20:59:OLD:        <snapshot/>
    2020-11-20 09:20:59:OLD:        <elastic/>
    2020-11-20 09:20:59:OLD:        <query/>
    2020-11-20 09:20:59:OLD:        <procedure/>
    2020-11-20 09:20:59:OLD:        <resourcemonitor>
    2020-11-20 09:20:59:OLD:            <memorylimit/>
    2020-11-20 09:20:59:OLD:        </resourcemonitor>
    2020-11-20 09:20:59:OLD:        <flushinterval>
    2020-11-20 09:20:59:OLD:            <dr/>
    2020-11-20 09:20:59:OLD:            <export/>
    2020-11-20 09:20:59:OLD:        </flushinterval>
    2020-11-20 09:20:59:OLD:    </systemsettings>
    2020-11-20 09:20:59:OLD:    <security/>
    2020-11-20 09:20:59:NEW:   <export>
           <configuration target="USER_TRANSACTIONS" enabled="true" type="file">
                <property name="type">csv</property>
                <property name="nonce">user_transactions</property>
                <property name="outdir">/Users/drolfe/Desktop</property>
            </configuration>
        </export>
   
    2020-11-20 09:20:59:OLD:</deployment>
    2020-11-20 09:20:59:<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <deployment>
        <cluster hostcount="1"/>
        <partition-detection/>
        <heartbeat/>
        <ssl/>
        <httpd enabled="true">
            <jsonapi enabled="true"/>
        </httpd>
        <snapshot enabled="false"/>
        <commandlog enabled="false">
            <frequency/>
        </commandlog>
        <systemsettings>
            <temptables/>
            <snapshot/>
            <elastic/>
            <query/>
            <procedure/>
            <resourcemonitor>
                <memorylimit/>
            </resourcemonitor>
            <flushinterval>
                <dr/>
                <export/>
            </flushinterval>
        </systemsettings>
        <security/>
       <export>
            <configuration target="USER_TRANSACTIONS" enabled="true" type="file">
                <property name="type">csv</property>
                <property name="nonce">user_transactions</property>
                <property name="outdir">/Users/drolfe/Desktop</property>
            </configuration>
        </export>
    </deployment>

    2020-11-20 09:20:59:Changed


    
