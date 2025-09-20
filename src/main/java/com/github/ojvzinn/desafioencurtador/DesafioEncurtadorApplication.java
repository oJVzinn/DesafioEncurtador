package com.github.ojvzinn.desafioencurtador;

import com.github.ojvzinn.desafioencurtador.entity.ShortenerEntity;
import com.github.ojvzinn.sqlannotation.SQLAnnotation;
import com.github.ojvzinn.sqlannotation.entity.MySQLEntity;
import com.github.ojvzinn.sqlannotation.entity.SQLConfigEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioEncurtadorApplication {

	public static void main(String[] args) {
		MySQLEntity mySQL = new MySQLEntity("localhost", 3306, "server", "root", "");
		SQLConfigEntity config = new SQLConfigEntity(mySQL);
		config.setLog(true);
		SQLAnnotation.init(config);
		SQLAnnotation.scanEntity(ShortenerEntity.class);

		SpringApplication.run(DesafioEncurtadorApplication.class, args);
	}

}
