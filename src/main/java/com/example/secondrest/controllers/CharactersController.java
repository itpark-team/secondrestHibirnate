package com.example.secondrest.controllers;

import com.example.secondrest.models.entities.Character;
import com.example.secondrest.models.tables.TableCharacters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/characters", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CharactersController {

    @Autowired
    private final TableCharacters tableCharacters;

    public CharactersController(TableCharacters tableCharacters) {
        this.tableCharacters = tableCharacters;
    }

    private void checkApiKey(String apiKey) throws Exception {
        String originalApiKey = "1212";

        if (originalApiKey.equals(apiKey) == false) {
            throw new Exception("Ошибка неверный API ключ");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("ERROR: " + exception.getMessage());
    }


    @GetMapping(value = "/getAll")
    public List<Character> getAll(@RequestHeader("APIKEY") String apiKey) throws Exception {
        checkApiKey(apiKey);

        return tableCharacters.findAll();
    }

    @GetMapping(value = "/getAllWithCname")
    public List<Character> getAllWithCname(@RequestHeader("APIKEY") String apiKey) throws Exception {
        checkApiKey(apiKey);

        return tableCharacters.findWhereNameStartsFromC();
    }

    @GetMapping(value = "/getById/{id}")
    public Character getById(@RequestHeader("APIKEY") String apiKey, @PathVariable int id) throws Exception {
        checkApiKey(apiKey);

        return tableCharacters.findById(id).get();
    }

    @PostMapping(value = "/insertOne")
    public void insertOne(@RequestHeader("APIKEY") String apiKey, @RequestBody Character character) throws Exception {
        checkApiKey(apiKey);

        tableCharacters.save(character);
    }

    @PutMapping(value = "/updateById/{id}")
    public void updateById(@RequestHeader("APIKEY") String apiKey, @PathVariable int id, @RequestBody Character character) throws Exception {
        checkApiKey(apiKey);

        Character findedCharacter = tableCharacters.findById(id).get();

        findedCharacter.name = character.name;
        findedCharacter.rating = character.rating;

        tableCharacters.save(findedCharacter);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public void deleteById(@RequestHeader("APIKEY") String apiKey, @PathVariable int id) throws Exception {
        checkApiKey(apiKey);

        tableCharacters.deleteById(id);
    }


}

