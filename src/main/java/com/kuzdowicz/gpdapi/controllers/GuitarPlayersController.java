package com.kuzdowicz.gpdapi.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kuzdowicz.gpdapi.constants.Formats;
import com.kuzdowicz.gpdapi.models.domain.Guitar;
import com.kuzdowicz.gpdapi.models.domain.GuitarPlayer;
import com.kuzdowicz.gpdapi.models.forms.AddGuitarPlayerForm;
import com.kuzdowicz.gpdapi.repositories.GuitarPlayersRepository;
import com.kuzdowicz.gpdapi.repositories.GuitarsRepository;

@RestController
@RequestMapping("/guitar-players")
public class GuitarPlayersController {

	private final static Logger logger = Logger.getLogger(GuitarPlayersController.class);

	private GuitarPlayersRepository guitarPlayersRepository;

	private GuitarsRepository guitarsRepository;

	@Autowired
	public GuitarPlayersController(GuitarPlayersRepository guitarPlayersRepository,
			GuitarsRepository guitarsRepository) {
		this.guitarPlayersRepository = guitarPlayersRepository;
		this.guitarsRepository = guitarsRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<GuitarPlayer> getAllGuitarPlayers() {
		return guitarPlayersRepository.findAll();
	}

	@RequestMapping(value = "/age/{ageVal}", method = RequestMethod.GET)
	public List<GuitarPlayer> getAllByAge(@PathVariable int ageVal) {
		return guitarPlayersRepository.findByAge(ageVal);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void addOne(@RequestBody AddGuitarPlayerForm addGuitarPlayerForm) {

		GuitarPlayer newGuitarPlayer = new GuitarPlayer();
		newGuitarPlayer.setName(addGuitarPlayerForm.getName());
		newGuitarPlayer.setLastname(addGuitarPlayerForm.getLastname());
		newGuitarPlayer.setAge(addGuitarPlayerForm.getAge());
		newGuitarPlayer.setHeIsAlive(addGuitarPlayerForm.getHeIsAlive());
		newGuitarPlayer.setNationality(addGuitarPlayerForm.getNationality());

		SimpleDateFormat dateFormatter = new SimpleDateFormat(Formats.GUITAR_PLAYER_DATE_FORMAT);
		try {
			newGuitarPlayer.setDateOfBirth(dateFormatter.parse(addGuitarPlayerForm.getDateOfBirth()));
		} catch (ParseException e) {
			logger.debug(e);
		}

		Guitar guitar = guitarsRepository.findOne(addGuitarPlayerForm.getGuitarId());
		newGuitarPlayer.setGuitars(Arrays.asList(guitar));
		guitarPlayersRepository.save(newGuitarPlayer);

	}

}
