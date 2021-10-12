# Что это и зачем нужно?
Иногда хочется добавить в программу что-то наподобие окна приветствия, предшествующее основной программе. Данный проект является вот таким окном приветствия.

Окно написано на javaFX и представляет из себя заготовку для использования в javaFX проектах.

# Кратко о возможностях
Методы класса `GreetingWindow` предоставляют следующие возможности:
* создание отдельного окна и анимации для него;
* преврарительная настройка **некоторых** *(см. раздел ограничений)* параметров окна и анимации средствами `GreetingWindow`;
* возможность подписаться на событие завершения анимации;
* механизм закрытия окна после завершения анимации *(по желанию)*.

# Как это использовать?

## Основные классы для взаимодействия
Несмотря на то, что в проекте несколько файлов, взаимодействие с пользователем возложено на класс [GreetingWindow.java](src/main/java/nongroup/GreetingWindow.java).
Также подразумевается использование перечисления [AnimaTarget.java](src/main/java/nongroup/AnimaTarget.java), которое позволяет определить, для чего необходимо создать анимацию.

## Простой пример использования
Сперва необходимо создать объект `GreetingWindow`, после чего окно можно предварительно настроить, далее есть несколько вариантов взаимодействия:
1. Создать `Stage` (окно) и анимацию к нему по отдельности, после чего их запустить.
Пример:
```
    GreetingWindow gw = new GreetingWindow();
    Stage stage = gw.createGreetingWindow();
    Animation animation = gw.createAnimation(AnimaTarget.BOTH, AnimaTarget.ONLY_TEXT);
    stage.show();
    animation.play();
```

Или можно вот так:
```
    GreetingWindow gw = new GreetingWindow();
    Stage stage = gw.createGreetingWindow();
    Animation animation = gw.createAnimation(AnimaTarget.BOTH, AnimaTarget.ONLY_TEXT);
    stage.setOnShowing((event) -> animation.play());
    stage.show();
```

2. Вызвать метод, который создаст `Stage`, `Animation`, и привяжет запуск анимации к появлению окна.
Пример:
```
    GreetingWindow gw = new GreetingWindow();
    Stage stage = gw.createStageWithAnimationOnShowing(AnimaTarget.ONLY_WINDOW, AnimaTarget.ONLY_WINDOW);
    stage.show();
```

## Более подробные примеры
Создание текста с тенью, но без анимации:
```
    GreetingWindow gw = new GreetingWindow();
    gw.enableShadowOnText = true;
    Stage stage = gw.createGreetingWindow();
    stage.show();
```

Создание окна, настроив предварительно текст, анимацию, окно, подписавшись на окончание анимации, и включив автоматические закрытие окна после анимации:
```
    GreetingWindow gw = new GreetingWindow();
    Color newText = gw.getSceneBackground();
    Color newBackground = gw.getTextColor();
    gw.setText("Hello there")
        .setTimeOfWindowAppearanceInMills(1000)
        .setTimeOfWindowDisappearanceInMills(1500)
        .setSceneBackground(newBackground)
        .setTextColor(newText)
        .observersList.add(() -> System.out.println("MEssage about animation end!"));
    gw.enableShadowOnText = false;
    Stage stage = gw.createStageWithAnimationOnShowing(AnimaTarget.BOTH, AnimaTarget.BOTH, new int[] {0, 1000}, true);   
    stage.show();
```

Создание окна без анимаций и его закрытие через создание анимации задержки.
```
    GreetingWindow gw = new GreetingWindow();
    Stage stage = gw.createGreetingWindow();
    Animation delay = gw.createDelay(3000);
    delay.setOnFinished((event) -> stage.close());
    stage.setOnShowing((event) -> delay.play());
    stage.show();
```

# Более подробно про ограничения и возможности

## Настройка окна и анимации средствами `GreetingWindow`
У окна можно настроить следующие характеристики:
* цвет текста, цвет фона, цвет тени текста;
* включить/выключить тень вокруг текста (из-за неё тормозит анимация);
* текст, выводимый окном;
* иконку окна.

У анимации можно настроить следующие характеристики:
* задержки между анимация;
* время анимации появления/исчезновения окна/текста;
* что именно будет анимировано во время появления и во время исчезновения (например, использовать анимацию появления для окна и текста, и не использовать анимацию исчезновения вовсе).

## Ограничения
* некоторые настройки окна `GreetingWindow` не подразумевает, например, изменение размеров или расположения окна приветствия. Однако это можно настроить вручную, получив через один из методов объект `Stage` и настроив некоторые его параметры до вызова `stageObj.show()`;
* предусмотрен только 1 вид анимация: постепенное изменение прозрачности;
* предусмотрен только 1 элемент на форме - `Label` с текстом;
* окно не закроется автоматически, если не были созданы анимации; по этой же причине наблюдатели вызваны не будут, так как ничего не заканчивается;
* если указать все анимации NO_ANIMATION, но передать при этом задержки, последние проигнорируются и анимации задержек созданы не будут.

## Важные особенности
* настраивать окно необходимо до создания `Stage`;
* настраивать анимацию необходимо до создания `Animation`, либо до вызова метода, который возвращает `Stage` с вшитой в событие появления окна анимацией;
* все сеттеры возвращают ссылку на этот же объект. Сделано, чтобы было удобно вызывать несколько методов подряд.

## Как обойти некоторые ограничения
Так как `GreetingWindow` возвращает как объект `Stage`, так и `Animation`, провести более тонкую настройку можно работая напрямую с ними.

# Скриншоты окна

## Вид без изменений
![здесь должен быть скриншот, но он куда-то пропал](src/main/resources/screenshots/screenshot1.PNG)

## Изменения вида средствами `GreetingWindow`
![здесь должен быть скриншот, но он куда-то пропал](src/main/resources/screenshots/screenshot2.PNG)


