package org.liujiaxin.jrpc.core;

public class InterfaceInfo {

    private String interfaceName;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public boolean equals(Object obj) {

        return super.equals(obj) || interfaceName.equals(((InterfaceInfo) obj).getInterfaceName());
    }

    @Override
    public int hashCode() {
        return interfaceName.hashCode();
    }
}
