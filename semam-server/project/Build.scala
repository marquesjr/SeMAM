import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "angular-play"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    javaCore,
  	javaEbean,
    "commons-io" % "commons-io" % "1.3.2",
    "org.drools" % "knowledge-api" % "5.5.0.Final",
    "org.drools" % "drools-core" % "5.5.0.Final",
    "org.drools" % "drools-compiler" % "5.5.0.Final",
    "br.ufes.inf.lprm" % "situation-api" % "0.7.0",
    "br.ufes.inf.lprm" % "scene-core" % "0.8.0",
    "com.google.code.gson" % "gson" % "2.2.4",
    "org.jsoup" % "jsoup" % "1.7.2"
  )
  
  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "JBoss Public Repository Group" at "http://repository.jboss.org/nexus/content/groups/public/",
    resolvers += "Situation API Repo" at "https://raw.github.com/pereirazc/SCENE/mvn-repo",
    resolvers += "Maven Repo" at "http://repo1.maven.org/maven2/"
  )

}
