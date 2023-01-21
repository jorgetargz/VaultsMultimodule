module Security {
    requires com.google.common;
    requires lombok;
    requires org.apache.logging.log4j;
    requires jakarta.jakartaee.web.api;

    exports org.jorgetargz.security;
    exports org.jorgetargz.security.impl;

}
