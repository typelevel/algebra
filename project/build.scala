import sbt._
import sbt.Keys._

object AlgebraBuild extends Build {

  lazy val core =
    Project("core", file("core"))
      .settings(algebraSettings: _*)
      .settings(
        unmanagedSourceDirectories in Compile +=
          (sourceDirectory in Compile).value / s"scala_spec"
      )

  lazy val std =
    Project("std", file("std")).settings(algebraSettings: _*).dependsOn(core)

  lazy val laws =
    Project("laws", file("laws")).settings(algebraSettings: _*).dependsOn(core, std)

  lazy val aggregate =
    Project("aggregate", file(".")).settings(aggregateSettings: _*).dependsOn(core, std, laws)

  lazy val coreMinibox =
    Project("core-minibox", file("core-minibox"))
      .settings(algebraSettings: _*)
      .settings(Minibox.miniboxed(core): _*)

  lazy val stdMinibox =
    Project("std-minibox", file("std-minibox"))
      .settings(algebraSettings: _*)
      .settings(Minibox.miniboxed(std): _*)
      .dependsOn(coreMinibox)

  lazy val algebraSettings =
    Defaults.defaultSettings ++ Publish.settings

  lazy val aggregateSettings =
    algebraSettings ++ Publish.noPublishSettings
}
