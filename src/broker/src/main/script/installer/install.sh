#! /bin/sh
# ${pom.artifactId} install script.
###

echo Adding user...
/usr/sbin/groupadd bronzethistle
/usr/sbin/useradd -g bronzethistle bronzethistle

echo Creating log folders...
mkdir -p /var/log/bronzethistle/${pom.artifactId}
/bin/chown -R bronzethistle:bronzethistle /var/log/bronzethistle/${pom.artifactId}
chmod -R 755 /var/log/bronzethistle/${pom.artifactId}

echo Creating log folders for pid...
mkdir -p $INSTALL_PATH/logs
/bin/chown -R bronzethistle:bronzethistle $INSTALL_PATH/logs
chmod -R 755 $INSTALL_PATH/logs

echo Configuring application...
/bin/chown -R bronzethistle:bronzethistle $INSTALL_PATH
/bin/chmod 755 $INSTALL_PATH/bin/${pom.artifactId}
/bin/chmod 755 $INSTALL_PATH/bin/wrapper-*
echo Done.

echo Installing service...
ln -s $INSTALL_PATH/bin/${pom.artifactId} /etc/init.d/${pom.artifactId}
/bin/chmod 755 /etc/init.d/${pom.artifactId}
/sbin/chkconfig --add ${pom.artifactId}
echo Done.