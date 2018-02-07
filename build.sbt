name := "YMLD-server"

version := "0.1"

scalaVersion := "2.12.4"

val akkaHttpVersion = "10.0.9"
val akkaHttpCoreVersion = "10.0.9"
val akkaActorVersion = "2.4.19"
val akkaStreamVersion = "2.0.2"
val apacheDerbyNetVersion = "10.14.1.0"
val apacheDerbyClientVersion = "10.14.1.0"
val slickVersion = "3.2.1"
val slickHikaricpVersion = "3.2.1"
val swaggerAkkaHttpVersion = "0.11.0"
val swaggerUiAkkaHttpVersion = "1.1.0"
val json4sVersion = "3.5.3"
val scalaTestVersion = "10.0.6"
val akkaSlf4jVersion = "2.4.19"
val logbackClassicVersion = "1.2.3"
val googleGuiceVersion = "3.0"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Xlint",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-feature",
  "-language:_"
)

libraryDependencies ++= Seq(
  // -- Akka-Http --
  "com.typesafe.akka" % "akka-http_2.12" % akkaHttpVersion,
  "com.typesafe.akka" % "akka-http-core_2.12" % akkaHttpCoreVersion,

  // -- Akka-Actor --
  "com.typesafe.akka" % "akka-actor_2.12" % akkaActorVersion,

  // -- Apache Derby Network Server --
  "org.apache.derby" % "derbynet" % apacheDerbyNetVersion,
  "org.apache.derby" % "derbyclient" % apacheDerbyClientVersion,

  // -- Slick --
  "com.typesafe.slick" % "slick_2.12" % slickVersion,
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % slickHikaricpVersion,
  "com.typesafe.slick" %% "slick-codegen" % "3.2.0",


  // -- Swagger --
  "com.github.swagger-akka-http" % "swagger-akka-http_2.12" % swaggerAkkaHttpVersion,
  "co.pragmati" %% "swagger-ui-akka-http" % swaggerUiAkkaHttpVersion,

  // -- Json4s --
  "org.json4s" % "json4s-native_2.12" % json4sVersion,
  "org.json4s" % "json4s-jackson_2.12" % json4sVersion,
  "org.json4s" % "json4s-ext_2.12" % json4sVersion,

  // -- Google Guice --
  "com.google.inject" % "guice" % googleGuiceVersion,

  // -- SLF4J Logging --
  "com.typesafe.akka" % "akka-slf4j_2.12" % akkaSlf4jVersion,
  // "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
)