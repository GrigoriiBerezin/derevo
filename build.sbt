name := "derevo"

val common = List(
  scalaVersion  := "2.13.0",
  crossScalaVersions := List("2.12.9", "2.13.0"),
  libraryDependencies += scalaOrganization.value % "scala-reflect" % scalaVersion.value % Provided,
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 11 | 12)) => List(compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch))
      case _                  => List()
    }
  },
  libraryDependencies += compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
  scalacOptions ++= Vector(
    "-deprecation",
    "-feature",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-Xfatal-warnings",
  ),
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, y)) if y == 11 => Seq("-Xexperimental")
      case Some((2, y)) if y == 13 => Seq("-Ymacro-annotations")
      case _                       => Seq.empty[String]
    }
  }
)



val compile211 = crossScalaVersions += "2.11.12"

lazy val core = project settings common settings compile211

lazy val cats          = project dependsOn core settings common
lazy val circe         = project dependsOn core settings common settings compile211
lazy val ciris         = project dependsOn core settings common settings (scalacOptions -= "-Xfatal-warnings")
lazy val tethys        = project dependsOn core settings common settings compile211
lazy val tschema       = project dependsOn core settings common settings compile211
lazy val reactivemongo = project dependsOn core settings common settings compile211
lazy val catsTagless   = project dependsOn core settings common settings compile211
lazy val pureconfig    = project dependsOn core settings common settings compile211

lazy val derevo = project in file(".") settings (common, publish := {}, publishLocal := {}) aggregate (
  core, cats, circe, ciris, tethys, tschema, reactivemongo, catsTagless, pureconfig
)
