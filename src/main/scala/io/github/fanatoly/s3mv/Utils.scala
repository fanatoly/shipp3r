package io.github.fanatoly.s3mv

import java.nio.file._
import scala.collection.mutable.Buffer

object DirectoryScanner{
  val cnt = FileVisitResult.CONTINUE
  def listFiles(localPath: Path): List[Path] = {
    val result = Buffer[Path]()
    Files.walkFileTree(localPath, new FileVisitor[Path]{
      def postVisitDirectory(x: Path,y: java.io.IOException): FileVisitResult = cnt
      def preVisitDirectory(x: Path,y: attribute.BasicFileAttributes): FileVisitResult = cnt
      def visitFile(p: Path,attrs: attribute.BasicFileAttributes): FileVisitResult = {
        if(attrs.isRegularFile) result.append(p)
        cnt
      }
      def visitFileFailed(x: Path,y: java.io.IOException): FileVisitResult = cnt
    })
    result.toList
  }
}

case class S3Upload(local: Path, s3Path: String)

object S3FileMapper{
  def mapPaths(base: Path, s3base: String, paths: List[Path]): Set[S3Upload] = {
    val s3Prefix = stripTrailingSlash(s3base) + "/"
    paths.map{ path =>
      S3Upload(path, s3Prefix + base.relativize(path).toString)
    }.toSet
  }

  private def stripTrailingSlash(str: String): String = {
    if(!str.isEmpty && str(str.length - 1) == '/') return str.substring(0, str.length - 1)
    else str
  }
}
