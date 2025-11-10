package anton.asmirko.app.service;

import anton.asmirko.app.model.user.User;
import anton.asmirko.app.state.app.AppState;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateService {

  private final AppState appState;
  private final ObjectMapper objectMapper;

  public void saveToFile(Path path) {
    final Set<User> users = appState.getUsers();
    try {
      final String jsonRep = objectMapper.writeValueAsString(users);
      Files.writeString(path, jsonRep, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Ошибка при сохранении состояния в файл");
    }
  }

  public void loadState(Path path) {
    try {
      final String jsonRep = Files.readString(path);
      final List<User> users = objectMapper.readValue(jsonRep, new TypeReference<>() {});
      appState.load(users);
    } catch (IOException e) {
      throw new IllegalStateException("Ошибка при загрузку состояния из файла");
    }
  }
}
