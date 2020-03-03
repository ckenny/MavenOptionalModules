# WAR Obfuscation Using ProGuard  

### Introduction

Here in this project, ProGuard is used and configured in combination with few plugins to achieve obfuscation of WAR file.

The ProGuard version 6.2.2 has been used in this project.

The ProGuard obfuscation has a limitation that it renames classes with alphabets in order. And the naming will start from `a` on a new container (i.e. package change, naming dictionary reset). It makes it easy to crack or, we can say, re-engineer to understand the logical flow.

To get rid of this limitation, a customized ProGuard version 6.2.2.1 has been used here. 

Refer:  https://sourceforge.net/p/proguard/feature-requests/191/#5d5f
    

### In-Depth

#### ProGuard Configuration

There is a maven plugin `proguard-maven-plugin` to perform obfuscation on jar files. But in our case where we want to obfuscate classes in WAR file, only this plugin will not work.

To achieve our purpose, we have to use a combination of plugins which will perform steps. The plugins used here are,

- exec-maven-plugin
- proguard-maven-plugin    
- maven-war-plugin


##### Plugin: exec-maven-plugin
Here is a part of POM file showing use of `exec-maven-plugin`. It is used in our case to create a dictionary text file. That is used as an input parameter of ProGuard obfuscation.
```xml
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>1.6.0</version>
        <executions>
            <execution>
                <id>members-dictionary-generator</id>
                <phase>process-classes</phase>
                <goals>
                    <goal>java</goal>
                </goals>
                <configuration>
                    <mainClass>com.study.packaging.DictionaryGenerator</mainClass>
                    <arguments>
                        <argument>${project.build.directory}/members-dictionary.txt</argument>
                        <argument>500</argument>
                    </arguments>
                </configuration>
            </execution>
            ...
    
        </executions>
    </plugin>
```

##### Plugin: proguard-maven-plugin
Here is a part of POM file showing use of `proguard-maven-plugin`.

Look at the `injar` and `outjar`, directories are configured there instead of JAR file. This will serve our purpose to obfuscate classes first and then that directory will be used in WAR packaging.

The other important part in this configuration is that, few options are customized not available in ProGuard (`-dontresetmembernaming -dontresetclassnaming -dontresetpackagenaming`).

And, the dictionary options, that uses the text files created by previous exec plugin to make naming dynamic everytime in obfuscation.
```xml
    <plugin>
        <groupId>com.github.wvengen</groupId>
        <artifactId>proguard-maven-plugin</artifactId>
        <version>${proguard.maven.plugin}</version>
        <configuration>
            <injar>classes</injar>
            <outjar>proguardClasses</outjar>
            <obfuscate>true</obfuscate>
            <options>
                ...
                <option>-dontresetmembernaming</option>
                <option>-dontresetclassnaming</option>
                <option>-dontresetpackagenaming</option>
    
                <option>-obfuscationdictionary '${project.build.directory}/members-dictionary.txt'</option>
                <option>-classobfuscationdictionary '${project.build.directory}/classes-dictionary.txt'</option>
                <option>-packageobfuscationdictionary '${project.build.directory}/packages-dictionary.txt'</option>
                ...
            </options>
            <injarNotExistsSkip>true</injarNotExistsSkip>
        </configuration>
        <executions>
            <execution>
                <phase>process-classes</phase>
                <goals>
                    <goal>proguard</goal>
                </goals>
            </execution>
        </executions>
        <dependencies>
            <dependency>
                <groupId>net.sf.proguard</groupId>
                <artifactId>proguard-base</artifactId>
                <version>${proguard.version}</version>
            </dependency>
        </dependencies>
    </plugin>
```


##### Plugin: maven-war-plugin
Here is a part of POM file showing use of `maven-war-plugin`.

The important part in this configuration is, the web resource uses proguard obfuscated classes directory. So the WAR will be generated using classes and the obfuscated classes all together. 

To avoid the original classes in WAR packaging, `packagingExcludes` configuration is set with all the packages to exclude.
```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <version>3.2.2</version>
        <configuration>
            ...
            <packagingExcludes>
                WEB-INF/classes/com/study/packaging/,
                WEB-INF/classes/com/study/feature/,
                WEB-INF/classes/com/study/controller/,
                WEB-INF/classes/com/study/configs/
            </packagingExcludes>
            ...
            <webResources>
                <webResource>
                    <directory>${project.build.directory}/proguardClasses</directory>
                    <targetPath>WEB-INF/classes</targetPath>
                </webResource>
            </webResources>
            ...
        </configuration>
    </plugin>
```


