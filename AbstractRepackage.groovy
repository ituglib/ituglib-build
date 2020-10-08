import hudson.model.*;
import java.io.*;
import java.sql.*;

abstract class AbstractRepackage {
   String taskName = "[" + this.getClass().getSimpleName() + "] ";
   File archive;
   String version;
   String compression;

   public AbstractRepackage() {
   }

   abstract public void decompress(File target);

   abstract public void chown(File target);

   abstract public void chgrp(File target);

   abstract public void recompress(File source, File target);

   abstract public void clean(File target);

   public void setCompression(String value) {
      this.compression = value;
   }
}

