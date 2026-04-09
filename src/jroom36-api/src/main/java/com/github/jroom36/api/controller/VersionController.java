/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.api.controller;

import com.github.jroom36.api.dto.VersionView;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class VersionController {

	private final BuildProperties buildProperties;

	public VersionController(BuildProperties buildProperties) {
		this.buildProperties = buildProperties;
	}

	@GetMapping("/version")
	public VersionView getVersion() {
		return new VersionView(
				buildProperties.getVersion(),
				buildProperties.getName()
		);
	}
}
